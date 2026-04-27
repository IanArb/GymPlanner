package com.ianarbuckle.gymplanner.checkin

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.checkin.domain.CheckIn
import com.ianarbuckle.gymplanner.checkin.dto.CheckInRequestDto
import kotlinx.coroutines.CancellationException
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface CheckInRepository {
    suspend fun checkIn(trainerId: String, checkInTime: LocalDateTime): Result<CheckIn>
}

class DefaultCheckInRepository : CheckInRepository, KoinComponent {

    private val remoteDataSource: CheckInRemoteDataSource by inject()

    override suspend fun checkIn(trainerId: String, checkInTime: LocalDateTime): Result<CheckIn> {
        try {
            val response =
                remoteDataSource.checkIn(trainerId, CheckInRequestDto(checkInTime = checkInTime))
            return Result.success(response.toCheckIn())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.withTag("CheckInRepository").e("Error checking in: $ex")
            return Result.failure(ex)
        }
    }
}
