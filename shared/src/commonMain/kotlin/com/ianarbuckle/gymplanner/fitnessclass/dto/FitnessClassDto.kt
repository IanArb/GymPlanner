package com.ianarbuckle.gymplanner.fitnessclass.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FitnessClassDto(
    val dayOfWeek: String,
    val description: String,
    @SerialName("duration") val durationDto: DurationDto,
    val endTime: String,
    val imageUrl: String,
    val name: String,
    val startTime: String,
)

@Serializable data class DurationDto(val unit: String, val value: Int)
