package com.ianarbuckle.gymplanner.availability.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckAvailabilityDto(val personalTrainerId: String, val isAvailable: Boolean)
