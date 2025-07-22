package com.ianarbuckle.gymplanner.fitnessclass.domain

import com.ianarbuckle.gymplanner.fitnessclass.dto.DurationDto
import com.ianarbuckle.gymplanner.fitnessclass.dto.FitnessClassDto

object FitnessClassUiModelMapper {

    fun FitnessClassDto.transformToFitnessClass(): FitnessClass =
        FitnessClass(
            dayOfWeek = dayOfWeek,
            description = description,
            name = name,
            imageUrl = imageUrl,
            startTime = startTime,
            endTime = endTime,
            duration = durationDto.transformDuration(),
        )

    private fun DurationDto.transformDuration(): Duration = Duration(value = value, unit = unit)
}
