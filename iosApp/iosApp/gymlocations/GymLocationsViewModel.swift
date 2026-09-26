//
//  GymLocationsViewModel.swift
//  iosApp
//
//  Created by Ian Arbuckle on 24/09/2026.
//  Copyright © 2026 orgName. All rights reserved.
//

import Combine
import Foundation
import SharedGymPlanner

class GymLocationsViewModel: ObservableObject {

    @Published
    var uiState: GymLocationsUiState = .idle

    private let repository: GymLocationsRepository

    init(repository: GymLocationsRepository) {
        self.repository = repository
    }

    func loadGymLocations() async {
        await MainActor.run {
            uiState = .loading
        }

        do {
            let result = try await repository.fetchGymLocations()

            switch result {
            case let success as ApiResultSuccess<AnyObject>:
                // Convert Kotlin GymLocations to Swift GymLocation.
                let kotlinLocations = success.value as? [SharedGymPlanner.GymLocations] ?? []
                let locations = kotlinLocations.map { location in
                    GymLocation(
                        id: location.title,
                        title: location.title,
                        subTitle: location.subTitle,
                        description: location.description_,
                        imageUrl: location.imageUrl
                    )
                }

                await MainActor.run {
                    self.uiState = .success(locations)
                }

            case let failure as ApiResultFailure:
                print("Error loading gym locations: \(failure.error)")
                await MainActor.run {
                    self.uiState = .error
                }

            default:
                await MainActor.run {
                    self.uiState = .error
                }
            }
        } catch {
            print("Error loading gym locations: \(error)")
            await MainActor.run {
                uiState = .error
            }
        }
    }
}
