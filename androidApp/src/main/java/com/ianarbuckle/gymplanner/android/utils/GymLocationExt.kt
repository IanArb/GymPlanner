package com.ianarbuckle.gymplanner.android.utils

import com.ianarbuckle.gymplanner.common.GymLocation

fun GymLocation.toLocalisedString(): String = when (this) {
    GymLocation.UNKNOWN -> "Unknown"
    GymLocation.CLONTARF -> "Clontarf"
    GymLocation.DUNLOAGHAIRE -> "Dunlaoghaire"
    GymLocation.ASTONQUAY -> "Aston Quay"
    GymLocation.LEOPARDSTOWN -> "Leopardstown"
    GymLocation.SANDYMOUNT -> "Sandymount"
    GymLocation.WESTMANSTOWN -> "Westmanstown"
}
