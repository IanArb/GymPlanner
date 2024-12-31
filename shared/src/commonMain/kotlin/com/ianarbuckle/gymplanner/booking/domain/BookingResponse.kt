package com.ianarbuckle.gymplanner.booking.domain

data class BookingResponse(
    val userId: String,
    val clientName: String,
    val bookingDate: String,
    val bookingTime: String,
    val personalTrainerName: String,
    val status: BookingStatus,
)

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    UNKNOWN,
}
