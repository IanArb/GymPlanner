package com.ianarbuckle.gymplanner.availability.domain

import com.ianarbuckle.gymplanner.availability.dto.AvailabilityDto
import com.ianarbuckle.gymplanner.availability.dto.CheckAvailabilityDto
import com.ianarbuckle.gymplanner.availability.dto.SlotDto
import com.ianarbuckle.gymplanner.availability.dto.TimeDto
import kotlinx.collections.immutable.toImmutableList

object AvailabilityMapper {

    fun AvailabilityDto.toAvailability(): Availability =
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

    fun CheckAvailabilityDto.toCheckAvailability(): CheckAvailability =
        CheckAvailability(personalTrainerId = personalTrainerId, isAvailable = isAvailable)
}
