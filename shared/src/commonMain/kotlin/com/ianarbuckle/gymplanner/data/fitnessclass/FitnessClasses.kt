package com.ianarbuckle.gymplanner.data.fitnessclass

import com.ianarbuckle.gymplanner.model.FitnessClass
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface FitnessClasses {
    suspend fun fetchTodaysFitnessClasses(): Result<List<FitnessClass>>
    suspend fun fetchFitnessClasses(dayOfWeek: String): Result<List<FitnessClass>>
}

class DefaultFitnessClasses(private val repository: FitnessClassRepository) : FitnessClasses {

    override suspend fun fetchTodaysFitnessClasses(): Result<List<FitnessClass>> {
        val currentMoment: Instant = Clock.System.now()
        val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

        val dayOfWeek = datetimeInSystemZone.dayOfWeek

        when (dayOfWeek) {
            DayOfWeek.MONDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "MONDAY")
            }
            DayOfWeek.TUESDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "TUESDAY")
            }
            DayOfWeek.WEDNESDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "WEDNESDAY")
            }
            DayOfWeek.THURSDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "THURSDAY")
            }
            DayOfWeek.FRIDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "FRIDAY")
            }
            DayOfWeek.SATURDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "SATURDAY")
            }
            DayOfWeek.SUNDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "SUNDAY")
            }
            else -> {
                return Result.failure(exception = Exception("No classes found"))
            }
        }
    }

    private suspend fun fetchClassesByDayOfWeek(dayOfWeek: String): Result<List<FitnessClass>> {
        return repository.fetchFitnessClasses(dayOfWeek)
    }

    override suspend fun fetchFitnessClasses(dayOfWeek: String): Result<List<FitnessClass>> {
        return repository.fetchFitnessClasses(dayOfWeek)
    }
}