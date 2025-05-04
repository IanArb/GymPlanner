package com.ianarbuckle.gymplanner.booking.dto

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(
    val timeSlotId: String,
    val userId: String,
    val bookingDate: String,
    val startTime: LocalTime,
    val personalTrainer: PersonalTrainerDto,
)
