package com.ianarbuckle.gymplanner.booking.domain

import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val timeSlotId: String,
    val userId: String,
    val bookingDate: String,
    val startTime: LocalTime,
    val personalTrainer: PersonalTrainer,
)

@Serializable
data class PersonalTrainer(
    val id: String,
    val name: String,
    val imageUrl: String,
    val gymLocation: GymLocation,
)
