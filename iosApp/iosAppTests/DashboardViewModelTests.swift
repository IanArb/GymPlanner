//
//  DashboardViewModelTests.swift
//  iosAppTests
//

import Foundation
@testable import iosApp
import SharedGymPlanner
import Testing

struct DashboardViewModelTests {
    // MARK: - Helpers

    private func makeSUT() -> (DashboardViewModel, FakeFitnessClassRepository) {
        let repository = FakeFitnessClassRepository()
        let sut = DashboardViewModel(repository: repository)
        return (sut, repository)
    }

    private func successClasses(_ state: DashboardUiState) -> [iosApp.FitnessClass]? {
        if case let .success(classes) = state {
            return classes
        }
        return nil
    }

    private func isError(_ state: DashboardUiState) -> Bool {
        if case .error = state {
            return true
        }
        return false
    }

    // MARK: - Initial state

    @Test func initialStateIsIdle() {
        let (sut, _) = makeSUT()
        if case .idle = sut.uiState { } else {
            Issue.record("Expected initial state to be .idle, got \(sut.uiState)")
        }
    }

    // MARK: - Success

    @Test func loadClassesSuccessMapsMultipleClasses() async {
        let (sut, repository) = makeSUT()
        repository.resultToReturn = TestDataProvider.success(TestDataProvider.multipleClasses)

        await sut.loadClasses(dayOfWeek: "MONDAY")

        let classes = successClasses(sut.uiState)
        #expect(classes?.count == 3)
        #expect(classes?.map(\.name) == ["Yoga Flow", "HIIT", "Spin"])
        #expect(repository.requestedDays == ["MONDAY"])
    }

    @Test func loadClassesSuccessMapsFieldsAndParsesTimes() async {
        let (sut, repository) = makeSUT()
        repository.resultToReturn = TestDataProvider.success([
            TestDataProvider.kotlinFitnessClass(
                name: "Yoga Flow",
                description: "A gentle flow class.",
                imageUrl: "https://example.com/yoga.jpg",
                startTime: "16:00",
                endTime: "17:00"
            ),
        ])

        await sut.loadClasses(dayOfWeek: "MONDAY")

        let first = successClasses(sut.uiState)?.first
        #expect(first?.name == "Yoga Flow")
        #expect(first?.description == "A gentle flow class.")
        #expect(first?.imageUrl == "https://example.com/yoga.jpg")
        // id is synthesised from name + startTime.
        #expect(first?.id == "Yoga Flow-16:00")

        // "HH:mm" strings are parsed into Dates; verify the hour/minute components.
        let calendar = Calendar.current
        if let start = first?.startTime {
            #expect(calendar.component(.hour, from: start) == 16)
            #expect(calendar.component(.minute, from: start) == 0)
        } else {
            Issue.record("Expected a start time")
        }
        if let end = first?.endTime {
            #expect(calendar.component(.hour, from: end) == 17)
            #expect(calendar.component(.minute, from: end) == 0)
        } else {
            Issue.record("Expected an end time")
        }
    }

    @Test func loadClassesSuccessWithEmptyListYieldsEmptySuccess() async {
        let (sut, repository) = makeSUT()
        repository.resultToReturn = TestDataProvider.success([])

        await sut.loadClasses(dayOfWeek: "TUESDAY")

        #expect(successClasses(sut.uiState)?.isEmpty == true)
    }

    // MARK: - Failure

    @Test func loadClassesApiFailureYieldsError() async {
        let (sut, repository) = makeSUT()
        repository.resultToReturn = TestDataProvider.failure(message: "boom")

        await sut.loadClasses(dayOfWeek: "MONDAY")

        #expect(isError(sut.uiState))
    }

    // NB: We don't test the `do/catch` throw path here. In production the KMP
    // repository never throws (failures come back as `ApiResult.Failure` via
    // `runCatching`), and forcing a Swift fake to throw across the SKIE suspend
    // bridge crashes the runtime. The error → `.error` behaviour is covered by
    // `loadClassesApiFailureYieldsError`.

    // MARK: - Repeated calls

    @Test func loadClassesCanRecoverFromErrorOnSecondCall() async {
        let (sut, repository) = makeSUT()

        repository.resultToReturn = TestDataProvider.failure()
        await sut.loadClasses(dayOfWeek: "MONDAY")
        #expect(isError(sut.uiState))

        repository.resultToReturn = TestDataProvider.success(TestDataProvider.multipleClasses)
        await sut.loadClasses(dayOfWeek: "MONDAY")
        #expect(successClasses(sut.uiState)?.count == 3)
    }
}
