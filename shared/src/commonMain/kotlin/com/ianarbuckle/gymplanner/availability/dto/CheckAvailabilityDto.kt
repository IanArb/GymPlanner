package com.ianarbuckle.gymplanner.availability.dto

import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability
import kotlinx.serialization.Serializable

@Serializable
data class CheckAvailabilityDto(val personalTrainerId: String, val isAvailable: Boolean) {

    fun toCheckAvailability(): CheckAvailability =
        CheckAvailability(personalTrainerId = personalTrainerId, isAvailable = isAvailable)
}
