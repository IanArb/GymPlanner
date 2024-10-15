package com.ianarbuckle.gymplanner.mapper

import com.ianarbuckle.gymplanner.model.Duration
import com.ianarbuckle.gymplanner.model.FitnessClass
import com.ianarbuckle.gymplanner.data.fitnessclass.dto.DurationDto
import com.ianarbuckle.gymplanner.data.fitnessclass.dto.FitnessClassDto

object FitnessClassUiModelMapper {

    fun FitnessClassDto.transformToFitnessClass(): FitnessClass {
        return FitnessClass(
            dayOfWeek = dayOfWeek,
            description = description,
            name = name,
            imageUrl = imageUrl,
            startTime = startTime,
            endTime = endTime,
            duration = durationDto.transformDuration()
        )
    }

    private fun DurationDto.transformDuration(): Duration {
        return Duration(
            value = value,
            unit = unit
        )
    }
}