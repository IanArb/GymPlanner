package com.ianarbuckle.gymplanner.availability.dto

import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.availability.domain.Slot
import com.ianarbuckle.gymplanner.availability.domain.Time
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Serializable
data class AvailabilityDto(
    val id: String,
    val month: String,
    val personalTrainerId: String,
    val slots: List<SlotDto>,
) {
    fun toAvailability(): Availability =
        Availability(
            id = id,
            month = month,
            personalTrainerId = personalTrainerId,
            slots = slots.map { it.toSlot() }.toImmutableList(),
        )

    private fun SlotDto.toSlot(): Slot =
        Slot(date = date, id = id, times = times.map { it.toTime() }.toImmutableList())

    private fun TimeDto.toTime(): Time =
        Time(id = id, startTime = startTime, endTime = endTime, status = status)
}

@Serializable data class SlotDto(val id: String, val date: String, val times: List<TimeDto>)

@Serializable
data class TimeDto(val id: String, val endTime: String, val startTime: String, val status: String)
