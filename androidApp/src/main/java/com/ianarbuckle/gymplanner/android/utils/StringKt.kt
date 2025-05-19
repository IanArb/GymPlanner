package com.ianarbuckle.gymplanner.android.utils

import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

fun String.toGymLocation(default: GymLocation = GymLocation.UNKNOWN): GymLocation {
    return try {
        GymLocation.valueOf(this)
    } catch (e: IllegalArgumentException) {
        default
    }
}
