package com.ianarbuckle.gymplanner.checkin

import com.ianarbuckle.gymplanner.checkin.domain.CheckIn
import com.ianarbuckle.gymplanner.checkin.domain.CheckInStatus
import com.ianarbuckle.gymplanner.checkin.dto.CheckInRequestDto
import com.ianarbuckle.gymplanner.checkin.dto.CheckInResponseDto
import com.ianarbuckle.gymplanner.checkin.dto.CheckInStatusDto
import kotlinx.datetime.LocalDateTime

/** Provides test data for CheckInRepository tests */
object CheckInTestDataProvider {

    // ========== Trainer IDs ==========

    object TrainerIds {
        const val trainerOne = "670ebe2b45b5cf7b25243b0c"
        const val trainerTwo = "670ebe2b45b5cf7b25243b0d"
        const val nonExistentTrainer = "non-existent-trainer"
    }

    // ========== Check-In Times ==========

    object CheckInTimes {
        val onTime = LocalDateTime(2026, 4, 20, 8, 55, 0)
        val late = LocalDateTime(2026, 4, 20, 9, 30, 0)
        val morning = LocalDateTime(2026, 4, 21, 7, 0, 0)
    }

    // ========== Check-In Request DTOs ==========

    object CheckInRequestDtos {
        val onTime = CheckInRequestDto(checkInTime = CheckInTimes.onTime)
        val late = CheckInRequestDto(checkInTime = CheckInTimes.late)
    }

    // ========== Check-In Response DTOs ==========

    object CheckInResponseDtos {
        val onTime =
            CheckInResponseDto(
                id = "69e68ab76b61ed0d9c8d5579",
                trainerId = TrainerIds.trainerOne,
                checkInTime = CheckInTimes.onTime,
                status = CheckInStatusDto.ON_TIME,
            )

        val late =
            CheckInResponseDto(
                id = "69e68ab76b61ed0d9c8d5580",
                trainerId = TrainerIds.trainerOne,
                checkInTime = CheckInTimes.late,
                status = CheckInStatusDto.LATE,
            )

        val unknownStatus =
            CheckInResponseDto(
                id = "69e68ab76b61ed0d9c8d5581",
                trainerId = TrainerIds.trainerTwo,
                checkInTime = CheckInTimes.morning,
                status = CheckInStatusDto.UNKNOWN,
            )

        val withoutId =
            CheckInResponseDto(
                id = null,
                trainerId = TrainerIds.trainerOne,
                checkInTime = CheckInTimes.onTime,
                status = CheckInStatusDto.ON_TIME,
            )
    }

    // ========== Check-In Domain Models ==========

    object CheckIns {
        val onTime =
            CheckIn(
                id = "69e68ab76b61ed0d9c8d5579",
                trainerId = TrainerIds.trainerOne,
                checkInTime = CheckInTimes.onTime,
                status = CheckInStatus.ON_TIME,
            )

        val late =
            CheckIn(
                id = "69e68ab76b61ed0d9c8d5580",
                trainerId = TrainerIds.trainerOne,
                checkInTime = CheckInTimes.late,
                status = CheckInStatus.LATE,
            )

        val unknownStatus =
            CheckIn(
                id = "69e68ab76b61ed0d9c8d5581",
                trainerId = TrainerIds.trainerTwo,
                checkInTime = CheckInTimes.morning,
                status = CheckInStatus.UNKNOWN,
            )

        val withoutId =
            CheckIn(
                id = null,
                trainerId = TrainerIds.trainerOne,
                checkInTime = CheckInTimes.onTime,
                status = CheckInStatus.ON_TIME,
            )
    }

    // ========== Exceptions ==========

    object Exceptions {
        val networkError = Exception("Network unavailable")
        val serverError = RuntimeException("Internal server error")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val notFound = RuntimeException("Trainer not found")
        val validationError = IllegalArgumentException("Invalid check-in data")
    }
}
