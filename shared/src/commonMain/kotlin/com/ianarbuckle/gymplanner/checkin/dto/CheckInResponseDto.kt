package com.ianarbuckle.gymplanner.checkin.dto

import com.ianarbuckle.gymplanner.checkin.domain.CheckIn
import com.ianarbuckle.gymplanner.checkin.domain.CheckInStatus
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CheckInResponseDto(
    val id: String? = null,
    val trainerId: String,
    val checkInTime: LocalDateTime,
    val status: CheckInStatusDto,
) {
    fun toCheckIn(): CheckIn =
        CheckIn(
            id = this.id,
            trainerId = this.trainerId,
            checkInTime = this.checkInTime,
            status = this.status.toCheckInStatus(),
        )

    private fun CheckInStatusDto.toCheckInStatus(): CheckInStatus =
        when (this) {
            CheckInStatusDto.ON_TIME -> CheckInStatus.ON_TIME
            CheckInStatusDto.LATE -> CheckInStatus.LATE
            else -> CheckInStatus.UNKNOWN
        }
}

enum class CheckInStatusDto {
    ON_TIME,
    LATE,
    UNKNOWN,
}
