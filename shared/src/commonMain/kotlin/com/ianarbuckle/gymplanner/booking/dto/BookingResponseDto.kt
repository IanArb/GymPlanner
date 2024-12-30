package com.ianarbuckle.gymplanner.booking.dto

import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.serialization.Serializable

@Serializable
data class BookingResponseDto(
    val id: String? = null,
    val client: ClientDto,
    val bookingDate: String,
    val startTime: String,
    val personalTrainer: PersonalTrainerDto,
    val bookingStatus: BookingStatusDto,
)

@Serializable
data class ClientDto(
    val userId: String,
    val firstName: String,
    val surname: String,
    val email: String,
    val gymLocation: GymLocation,
)

@Serializable
data class PersonalTrainerDto(
    val id: String,
    val firstName: String,
    val surname: String,
    val imageUrl: String,
    val gymLocation: GymLocation,
)

enum class BookingStatusDto {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    UNKNOWN,
}
