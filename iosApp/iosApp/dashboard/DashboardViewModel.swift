//
//  DashboardViewModel.swift
//  iosApp
//
//  Created by Ian Arbuckle on 14/06/2026.
//  Copyright © 2026 orgName. All rights reserved.
//
import Combine
import Foundation
import SharedGymPlanner

class DashboardViewModel: ObservableObject {
    @Published
    var uiState: DashboardUiState = .idle

    private let repository: FitnessClassRepository

    init(repository: FitnessClassRepository) {
        self.repository = repository
    }

    func loadClasses(dayOfWeek: String) async {
        await MainActor.run {
            uiState = .loading
        }

        do {
            let result = try await repository.fetchFitnessClasses(dayOfWeek: dayOfWeek)

            switch result {
            case let success as ApiResultSuccess<AnyObject>:
                let fitnessClasses = success.value as? [SharedGymPlanner.FitnessClass] ?? []
                let classes = fitnessClasses.map { fitnessClass in
                    FitnessClass(
                        id: "\(fitnessClass.name)-\(fitnessClass.startTime)",
                        name: fitnessClass.name,
                        description: fitnessClass.description_,
                        imageUrl: fitnessClass.imageUrl,
                        startTime: Self.parseTime(fitnessClass.startTime),
                        endTime: Self.parseTime(fitnessClass.endTime)
                    )
                }

                await MainActor.run {
                    self.uiState = .success(classes)
                }

            case let failure as ApiResultFailure:
                print("Error loading classes: \(failure.error)")
                await MainActor.run {
                    self.uiState = .error
                }

            default:
                await MainActor.run {
                    self.uiState = .error
                }
            }
        } catch {
            print("Error loading classes: \(error)")
            await MainActor.run {
                uiState = .error
            }
        }
    }

    /// Parses a "HH:mm" time string (as returned by the API) into a `Date`.
    private static func parseTime(_ value: String) -> Date {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "HH:mm"
        return formatter.date(from: value) ?? Date()
    }
}
