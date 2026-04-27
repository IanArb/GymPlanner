package com.ianarbuckle.gymplanner.checkin.domain

import kotlinx.datetime.LocalDateTime

data class CheckIn(
    val id: String?,
    val trainerId: String,
    val checkInTime: LocalDateTime,
    val status: CheckInStatus,
)

enum class CheckInStatus {
    ON_TIME,
    LATE,
    UNKNOWN,
}
