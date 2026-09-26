//
//  GymLocationsViewModelTests.swift
//  iosAppTests
//

import Foundation
@testable import iosApp
import SharedGymPlanner
import Testing

struct GymLocationsViewModelTests {
    // MARK: - Helpers

    private func makeSUT() -> (GymLocationsViewModel, FakeGymLocationsRepository) {
        let repository = FakeGymLocationsRepository()
        let sut = GymLocationsViewModel(repository: repository)
        return (sut, repository)
    }

    private func successLocations(_ state: GymLocationsUiState) -> [iosApp.GymLocation]? {
        if case let .success(locations) = state {
            return locations
        }
        return nil
    }

    private func isError(_ state: GymLocationsUiState) -> Bool {
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

    @Test func loadGymLocationsSuccessMapsMultiple() async {
        let (sut, repository) = makeSUT()
        repository.resultToReturn = TestDataProvider.gymLocationsSuccess(TestDataProvider.multipleGymLocations)

        await sut.loadGymLocations()

        let locations = successLocations(sut.uiState)
        #expect(locations?.count == 3)
        #expect(locations?.map(\.title) == ["City Centre", "Northside", "Southside"])
        #expect(repository.fetchCallCount == 1)
    }

    @Test func loadGymLocationsSuccessMapsFields() async {
        let (sut, repository) = makeSUT()
        repository.resultToReturn = TestDataProvider.gymLocationsSuccess([
            TestDataProvider.kotlinGymLocation(
                title: "City Centre",
                subTitle: "Dublin 2",
                description: "Our flagship gym.",
                imageUrl: "https://example.com/gym.jpg"
            ),
        ])

        await sut.loadGymLocations()

        let first = successLocations(sut.uiState)?.first
        #expect(first?.title == "City Centre")
        #expect(first?.subTitle == "Dublin 2")
        #expect(first?.description == "Our flagship gym.")
        #expect(first?.imageUrl == "https://example.com/gym.jpg")
        // id is synthesised from the title.
        #expect(first?.id == "City Centre")
    }

    @Test func loadGymLocationsSuccessWithEmptyListYieldsEmptySuccess() async {
        let (sut, repository) = makeSUT()
        repository.resultToReturn = TestDataProvider.gymLocationsSuccess([])

        await sut.loadGymLocations()

        #expect(successLocations(sut.uiState)?.isEmpty == true)
    }

    // MARK: - Failure

    @Test func loadGymLocationsApiFailureYieldsError() async {
        let (sut, repository) = makeSUT()
        repository.resultToReturn = TestDataProvider.failure(message: "boom")

        await sut.loadGymLocations()

        #expect(isError(sut.uiState))
    }

    // MARK: - Repeated calls

    @Test func loadGymLocationsCanRecoverFromErrorOnSecondCall() async {
        let (sut, repository) = makeSUT()

        repository.resultToReturn = TestDataProvider.failure()
        await sut.loadGymLocations()
        #expect(isError(sut.uiState))

        repository.resultToReturn = TestDataProvider.gymLocationsSuccess(TestDataProvider.multipleGymLocations)
        await sut.loadGymLocations()
        #expect(successLocations(sut.uiState)?.count == 3)
    }
}
