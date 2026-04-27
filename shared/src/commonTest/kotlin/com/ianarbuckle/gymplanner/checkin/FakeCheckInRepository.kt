package com.ianarbuckle.gymplanner.checkin

import com.ianarbuckle.gymplanner.checkin.domain.CheckIn
import com.ianarbuckle.gymplanner.checkin.dto.CheckInRequestDto
import kotlinx.datetime.LocalDateTime

/** Fake implementation of CheckInRepository for testing */
class FakeCheckInRepository(private val remoteDataSource: CheckInRemoteDataSource) :
    CheckInRepository {

    override suspend fun checkIn(trainerId: String, checkInTime: LocalDateTime): Result<CheckIn> {
        return try {
            val response =
                remoteDataSource.checkIn(trainerId, CheckInRequestDto(checkInTime = checkInTime))
            Result.success(response.toCheckIn())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
