package com.ianarbuckle.gymplanner.android.utils

import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

fun GymLocation.toLocalisedString(): String {
    return when (this) {
        GymLocation.UNKNOWN -> "Unknown"
        GymLocation.CLONTARF -> "Clontarf"
        GymLocation.DUNLOAGHAIRE -> "Dunlaoghaire"
        GymLocation.ASTONQUAY -> "Aston Quay"
        GymLocation.LEOPARDSTOWN -> "Leopardstown"
        GymLocation.SANDYMOUNT -> "Sandymount"
        GymLocation.WESTMANSTOWN -> "Westmanstown"
    }
}
