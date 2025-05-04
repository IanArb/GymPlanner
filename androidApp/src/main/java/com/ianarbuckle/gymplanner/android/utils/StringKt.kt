package com.ianarbuckle.gymplanner.android.utils

import android.util.Log
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

fun String.toGymLocation(default: GymLocation = GymLocation.UNKNOWN): GymLocation {
    return try {
        GymLocation.valueOf(this)
    } catch (e: IllegalArgumentException) {
        Log.e("GymLocation", "Invalid GymLocation string: $this", e)
        default
    }
}
