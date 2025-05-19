package com.ianarbuckle.gymplanner.android.utils

import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

fun String.toGymLocation(): GymLocation {
    return GymLocation.valueOf(this)
}
