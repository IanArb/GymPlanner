//
//  DashboardUiState.swift
//  iosApp
//
//  Created by Ian Arbuckle on 14/06/2026.
//  Copyright © 2026 orgName. All rights reserved.
//

import SharedGymPlanner

enum DashboardUiState {
    case idle
    case success([FitnessClass])
    case error
    case loading
}
