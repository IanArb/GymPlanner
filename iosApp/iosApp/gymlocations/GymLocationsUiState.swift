//
//  GymLocationsUiState.swift
//  iosApp
//
//  Created by Ian Arbuckle on 24/09/2026.
//  Copyright © 2026 orgName. All rights reserved.
//

enum GymLocationsUiState {
    case idle
    case loading
    case success([GymLocation])
    case error
}
