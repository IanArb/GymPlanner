package com.ianarbuckle.gymplanner.booking.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(
    val client: ClientDto,
    val bookingDate: String,
    val startTime: String,
    val personalTrainer: PersonalTrainerDto,
)