//
//  DashboardViewModel.swift
//  iosApp
//
//  Created by Ian Arbuckle on 14/06/2026.
//  Copyright © 2026 orgName. All rights reserved.
//
import Foundation
import Combine
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

            // The repository returns a Kotlin `ApiResult` sealed type. It's exposed to Swift
            // as an existential, so match on its concrete SKIE-generated subtypes.
            switch result {
            case let success as ApiResultSuccess<AnyObject>:
                // Convert Kotlin FitnessClass to Swift FitnessClass
                let kotlinClasses = success.value as? [SharedGymPlanner.FitnessClass] ?? []
                let swiftClasses = kotlinClasses.map { kotlinClass in
                    FitnessClass(
                        id: "\(kotlinClass.name)-\(kotlinClass.startTime)",
                        name: kotlinClass.name,
                        description: kotlinClass.description_,
                        imageUrl: kotlinClass.imageUrl,
                        startTime: Self.parseTime(kotlinClass.startTime),
                        endTime: Self.parseTime(kotlinClass.endTime)
                    )
                }

                await MainActor.run {
                    self.uiState = .success(swiftClasses)
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

