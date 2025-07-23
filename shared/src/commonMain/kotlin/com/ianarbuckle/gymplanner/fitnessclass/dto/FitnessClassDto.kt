package com.ianarbuckle.gymplanner.fitnessclass.dto

import com.ianarbuckle.gymplanner.fitnessclass.domain.Duration
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
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
) {

    fun transformToFitnessClass(): FitnessClass =
        FitnessClass(
            dayOfWeek = dayOfWeek,
            description = description,
            name = name,
            imageUrl = imageUrl,
            startTime = startTime,
            endTime = endTime,
            duration = durationDto.transformDuration(),
        )
}

@Serializable
data class DurationDto(val unit: String, val value: Int) {

    fun transformDuration(): Duration = Duration(value = value, unit = unit)
}
