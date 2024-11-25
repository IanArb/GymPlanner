package com.ianarbuckle.gymplanner.booking.domain

import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val client: Client,
    val bookingDate: String,
    val startTime: String,
    val personalTrainer: PersonalTrainer,
)

@Serializable
data class Client(
    val userId: String,
    val firstName: String,
    val surname: String,
    val email: String,
    val gymLocation: GymLocation,
)

@Serializable
data class PersonalTrainer(
    val id: String,
    val firstName: String,
    val surname: String,
    val imageUrl: String,
    val gymLocation: GymLocation,
)
