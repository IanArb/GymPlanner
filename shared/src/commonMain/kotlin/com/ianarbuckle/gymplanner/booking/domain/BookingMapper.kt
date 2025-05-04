package com.ianarbuckle.gymplanner.booking.domain

import com.ianarbuckle.gymplanner.booking.dto.BookingDto
import com.ianarbuckle.gymplanner.booking.dto.BookingResponseDto
import com.ianarbuckle.gymplanner.booking.dto.BookingStatusDto
import com.ianarbuckle.gymplanner.booking.dto.PersonalTrainerDto

object BookingMapper {

    fun BookingResponseDto.toBookingResponse(): BookingResponse {
        return BookingResponse(
            userId = this.userId,
            timeSlotId = this.timeSlotId,
            bookingDate = this.bookingDate,
            startTime = this.startTime,
            personalTrainer = this.personalTrainer.toPersonalTrainer(),
            status = this.status.toBookingStatus(),
        )
    }

    private fun BookingStatusDto.toBookingStatus(): BookingStatus {
        return when (this) {
            BookingStatusDto.PENDING -> BookingStatus.PENDING
            BookingStatusDto.CONFIRMED -> BookingStatus.CONFIRMED
            BookingStatusDto.CANCELLED -> BookingStatus.CANCELLED
            BookingStatusDto.COMPLETED -> BookingStatus.COMPLETED
            else -> BookingStatus.UNKNOWN
        }
    }

    fun Booking.toBookingDto(): BookingDto {
        return BookingDto(
            timeSlotId = this.timeSlotId,
            userId = this.userId,
            bookingDate = this.bookingDate,
            startTime = this.startTime,
            personalTrainer = this.personalTrainer.toPersonalTrainerDto(),
        )
    }

    private fun PersonalTrainer.toPersonalTrainerDto(): PersonalTrainerDto {
        return PersonalTrainerDto(
            id = this.id,
            name = this.name,
            imageUrl = this.imageUrl,
            gymLocation = this.gymLocation,
        )
    }

    fun BookingDto.toBooking(): Booking {
        return Booking(
            timeSlotId = this.timeSlotId,
            userId = this.userId,
            bookingDate = this.bookingDate,
            startTime = this.startTime,
            personalTrainer = this.personalTrainer.toPersonalTrainer(),
        )
    }

    private fun PersonalTrainerDto.toPersonalTrainer(): PersonalTrainer {
        return PersonalTrainer(
            id = this.id,
            name = this.name,
            imageUrl = this.imageUrl,
            gymLocation = this.gymLocation,
        )
    }
}
