package com.ianarbuckle.gymplanner.booking.dto

import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import com.ianarbuckle.gymplanner.booking.domain.BookingStatus
import com.ianarbuckle.gymplanner.booking.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.serialization.Serializable

@Serializable
data class BookingResponseDto(
    val id: String? = null,
    val timeSlotId: String,
    val userId: String,
    val bookingDate: String,
    val startTime: String,
    val personalTrainer: PersonalTrainerDto,
    val status: BookingStatusDto,
) {
    fun toBookingResponse(): BookingResponse =
        BookingResponse(
            userId = this.userId,
            timeSlotId = this.timeSlotId,
            bookingDate = this.bookingDate,
            startTime = this.startTime,
            personalTrainer = this.personalTrainer.toPersonalTrainer(),
            status = this.status.toBookingStatus(),
        )

    private fun BookingStatusDto.toBookingStatus(): BookingStatus =
        when (this) {
            BookingStatusDto.PENDING -> BookingStatus.PENDING
            BookingStatusDto.CONFIRMED -> BookingStatus.CONFIRMED
            BookingStatusDto.CANCELLED -> BookingStatus.CANCELLED
            BookingStatusDto.COMPLETED -> BookingStatus.COMPLETED
            else -> BookingStatus.UNKNOWN
        }
}

@Serializable
data class PersonalTrainerDto(
    val id: String,
    val name: String,
    val imageUrl: String,
    val gymLocation: GymLocation,
) {

    fun toPersonalTrainer(): PersonalTrainer {
        return PersonalTrainer(
            id = this.id,
            name = this.name,
            imageUrl = this.imageUrl,
            gymLocation = this.gymLocation,
        )
    }
}

enum class BookingStatusDto {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    UNKNOWN,
}
