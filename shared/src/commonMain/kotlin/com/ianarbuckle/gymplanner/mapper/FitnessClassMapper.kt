package com.ianarbuckle.gymplanner.mapper

import com.ianarbuckle.gymplanner.model.Duration
import com.ianarbuckle.gymplanner.model.FitnessClass
import com.ianarbuckle.gymplanner.realm.DurationRealmDto
import com.ianarbuckle.gymplanner.realm.FitnessClassRealmDto
import io.realm.kotlin.notifications.ResultsChange

object FitnessClassMapper {

    fun FitnessClass.transformToFitnessRealm(): FitnessClassRealmDto {
        return FitnessClassRealmDto().apply {
            val fitnessClass = this@transformToFitnessRealm
            dayOfWeek = fitnessClass.dayOfWeek
            description = fitnessClass.description
            duration = mapDuration(fitnessClass.duration)
            startTime = fitnessClass.startTime
            endTime = fitnessClass.endTime
            imageUrl = fitnessClass.imageUrl
        }
    }

    private fun mapDuration(duration: Duration): DurationRealmDto {
        return DurationRealmDto().apply {
            value = duration.value
            unit = duration.unit
        }
    }

    fun ResultsChange<FitnessClassRealmDto>.transformClientDto(): List<FitnessClass> {
        return this.list.map { fitnessClass ->
            FitnessClass(
                dayOfWeek = fitnessClass.dayOfWeek,
                description = fitnessClass.description,
                duration = transformDuration(fitnessClass.duration),
                endTime = fitnessClass.endTime,
                startTime = fitnessClass.startTime,
                imageUrl = fitnessClass.imageUrl,
                name = fitnessClass.name
            )
        }
    }

    private fun transformDuration(duration: DurationRealmDto?): Duration {
        return Duration(
            value = duration?.value ?: 0,
            unit = duration?.unit ?: ""
        )
    }
}