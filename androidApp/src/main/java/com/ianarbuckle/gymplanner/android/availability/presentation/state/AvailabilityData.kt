package com.ianarbuckle.gymplanner.android.availability.presentation.state

import kotlinx.datetime.LocalTime

data class AvailabilityData(
    val selectedDate: String,
    val selectedTimeSlot: LocalTime,
    val timeSlotId: String,
)
