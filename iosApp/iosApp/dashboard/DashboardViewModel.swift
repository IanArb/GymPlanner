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
            
            switch result {
            case .success(let fitnessClasses):
                // Convert Kotlin FitnessClass to Swift FitnessClass
                let swiftClasses = fitnessClasses.map { kotlinClass in
                    FitnessClass(
                        id: kotlinClass.id,
                        name: kotlinClass.name,
                        description: kotlinClass.description_,
                        imageUrl: kotlinClass.imageUrl,
                        startTime: Date(timeIntervalSince1970: TimeInterval(kotlinClass.startTime / 1000)),
                        endTime: Date(timeIntervalSince1970: TimeInterval(kotlinClass.endTime / 1000))
                    )
                }
                
                await MainActor.run {
                    self.uiState = .success(swiftClasses)
                }
                
            case .failure(let error):
                print("Error loading classes: \(error)")
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
}

