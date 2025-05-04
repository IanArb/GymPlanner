package com.ianarbuckle.gymplanner.booking.domain

data class BookingResponse(
    val timeSlotId: String,
    val userId: String,
    val bookingDate: String,
    val startTime: String,
    val personalTrainer: PersonalTrainer,
    val status: BookingStatus,
)

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    UNKNOWN,
}
