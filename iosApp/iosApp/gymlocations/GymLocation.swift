//
//  GymLocation.swift
//  iosApp
//
//  Created by Ian Arbuckle on 24/09/2026.
//  Copyright © 2026 orgName. All rights reserved.
//

import Foundation

/// UI model for a single gym location, mapped from the shared Kotlin `GymLocations`.
struct GymLocation: Identifiable {
    let id: String
    let title: String
    let subTitle: String
    let description: String
    let imageUrl: String
}
