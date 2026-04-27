package com.ianarbuckle.gymplanner.checkin.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable data class CheckInRequestDto(val checkInTime: LocalDateTime)
