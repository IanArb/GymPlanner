package com.ianarbuckle.gymplanner.booking.domain

import com.ianarbuckle.gymplanner.booking.dto.BookingDto
import com.ianarbuckle.gymplanner.booking.dto.BookingResponseDto
import com.ianarbuckle.gymplanner.booking.dto.BookingStatusDto
import com.ianarbuckle.gymplanner.booking.dto.PersonalTrainerDto

object BookingMapper {

    fun BookingResponseDto.toBookingResponse(): BookingResponse =
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

    fun Booking.toBookingDto(): BookingDto =
        BookingDto(
            timeSlotId = this.timeSlotId,
            userId = this.userId,
            bookingDate = this.bookingDate,
            startTime = this.startTime,
            personalTrainer = this.personalTrainer.toPersonalTrainerDto(),
        )

    private fun PersonalTrainer.toPersonalTrainerDto(): PersonalTrainerDto =
        PersonalTrainerDto(
            id = this.id,
            name = this.name,
            imageUrl = this.imageUrl,
            gymLocation = this.gymLocation,
        )

    fun BookingDto.toBooking(): Booking =
        Booking(
            timeSlotId = this.timeSlotId,
            userId = this.userId,
            bookingDate = this.bookingDate,
            startTime = this.startTime,
            personalTrainer = this.personalTrainer.toPersonalTrainer(),
        )

    private fun PersonalTrainerDto.toPersonalTrainer(): PersonalTrainer =
        PersonalTrainer(
            id = this.id,
            name = this.name,
            imageUrl = this.imageUrl,
            gymLocation = this.gymLocation,
        )
}
