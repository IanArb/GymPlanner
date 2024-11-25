package com.ianarbuckle.gymplanner.booking.domain

import com.ianarbuckle.gymplanner.booking.dto.BookingDto
import com.ianarbuckle.gymplanner.booking.dto.BookingResponseDto
import com.ianarbuckle.gymplanner.booking.dto.BookingStatusDto
import com.ianarbuckle.gymplanner.booking.dto.ClientDto
import com.ianarbuckle.gymplanner.booking.dto.PersonalTrainerDto

object BookingMapper {

    fun BookingResponseDto.toBookingResponse(): BookingResponse {
        val clientName = this.client.firstName + " " + this.client.surname
        val personalTrainerName = this.personalTrainer.firstName + " " + this.personalTrainer.surname
        return BookingResponse(
            userId = this.client.userId,
            clientName = clientName,
            bookingDate = this.bookingDate,
            bookingTime = this.startTime,
            personalTrainerName = personalTrainerName,
            status = this.bookingStatus.toBookingStatus()
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
            client = this.client.toClientDto(),
            bookingDate = this.bookingDate,
            startTime = this.startTime,
            personalTrainer = this.personalTrainer.toPersonalTrainerDto()
        )
    }

    private fun Client.toClientDto(): ClientDto {
        return ClientDto(
            userId = this.userId,
            firstName = this.firstName,
            surname = this.surname,
            email = this.email,
            gymLocation = this.gymLocation
        )
    }

    private fun PersonalTrainer.toPersonalTrainerDto(): PersonalTrainerDto {
        return PersonalTrainerDto(
            id = this.id,
            firstName = this.firstName,
            surname = this.surname,
            imageUrl = this.imageUrl,
            gymLocation = this.gymLocation
        )
    }

    fun BookingDto.toBooking(): Booking {
        return Booking(
            client = this.client.toClient(),
            bookingDate = this.bookingDate,
            startTime = this.startTime,
            personalTrainer = this.personalTrainer.toPersonalTrainer()
        )
    }

    private fun ClientDto.toClient(): Client {
        return Client(
            userId = this.userId,
            firstName = this.firstName,
            surname = this.surname,
            email = this.email,
            gymLocation = this.gymLocation
        )
    }

    private fun PersonalTrainerDto.toPersonalTrainer(): PersonalTrainer {
        return PersonalTrainer(
            id = this.id,
            firstName = this.firstName,
            surname = this.surname,
            imageUrl = this.imageUrl,
            gymLocation = this.gymLocation
        )
    }
}