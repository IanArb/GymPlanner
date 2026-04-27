package com.ianarbuckle.gymplanner.checkin

import com.ianarbuckle.gymplanner.checkin.dto.CheckInRequestDto
import com.ianarbuckle.gymplanner.checkin.dto.CheckInResponseDto

/**
 * Fake implementation for testing CheckInRepository. Implements the CheckInRemoteDataSource
 * interface.
 */
class FakeCheckInRemoteDataSource : CheckInRemoteDataSource {

    var shouldThrowExceptionOnCheckIn = false
    var checkInException: Exception? = null

    val checkInCalls = mutableListOf<Pair<String, CheckInRequestDto>>()

    var checkInResponse: CheckInResponseDto = CheckInTestDataProvider.CheckInResponseDtos.onTime

    override suspend fun checkIn(
        trainerId: String,
        request: CheckInRequestDto,
    ): CheckInResponseDto {
        checkInCalls.add(trainerId to request)

        if (shouldThrowExceptionOnCheckIn) {
            throw checkInException ?: RuntimeException("Check-in failed")
        }

        return checkInResponse
    }

    fun reset() {
        shouldThrowExceptionOnCheckIn = false
        checkInException = null
        checkInCalls.clear()
        checkInResponse = CheckInTestDataProvider.CheckInResponseDtos.onTime
    }
}
