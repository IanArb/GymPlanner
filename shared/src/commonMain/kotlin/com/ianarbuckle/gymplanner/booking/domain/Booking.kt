package com.ianarbuckle.gymplanner.booking.domain

import com.ianarbuckle.gymplanner.booking.dto.BookingDto
import com.ianarbuckle.gymplanner.booking.dto.PersonalTrainerDto
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
) {
    fun toBookingDto(): BookingDto =
        BookingDto(
            timeSlotId = this.timeSlotId,
            userId = this.userId,
            bookingDate = this.bookingDate,
            startTime = this.startTime,
            personalTrainer = this.personalTrainer.toPersonalTrainerDto(),
        )
}

@Serializable
data class PersonalTrainer(
    val id: String,
    val name: String,
    val imageUrl: String,
    val gymLocation: GymLocation,
) {

    fun toPersonalTrainerDto(): PersonalTrainerDto =
        PersonalTrainerDto(
            id = this.id,
            name = this.name,
            imageUrl = this.imageUrl,
            gymLocation = this.gymLocation,
        )
}
