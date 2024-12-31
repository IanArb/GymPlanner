package com.ianarbuckle.gymplanner.availability.dto

import kotlinx.serialization.Serializable

@Serializable
data class AvailabilityDto(
    val id: String,
    val month: String,
    val personalTrainerId: String,
    val slots: List<SlotDto>,
)

@Serializable
data class SlotDto(
    val id: String,
    val date: String,
    val times: List<TimeDto>,
)

@Serializable
data class TimeDto(
    val id: String,
    val endTime: String,
    val startTime: String,
    val status: String,
)
