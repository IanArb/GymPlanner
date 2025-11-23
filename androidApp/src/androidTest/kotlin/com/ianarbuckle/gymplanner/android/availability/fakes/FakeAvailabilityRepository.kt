package com.ianarbuckle.gymplanner.android.availability.fakes

import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability

class FakeAvailabilityRepository : AvailabilityRepository {

    var shouldReturnError = false

    override suspend fun getAvailability(
        personalTrainerId: String,
        month: String,
    ): Result<Availability> {
        return if (shouldReturnError) {
            Result.failure(Exception("Error"))
        } else {
            mockAvailabilitySuccess(personalTrainerId = personalTrainerId, month = month)
        }
    }

    override suspend fun checkAvailability(
        personalTrainerId: String,
        month: String,
    ): Result<CheckAvailability> {
        return if (shouldReturnError) {
            Result.failure(Exception("Error"))
        } else {
            mockCheckAvailabilitySuccess(personalTrainerId = personalTrainerId)
        }
    }

    private fun mockAvailabilitySuccess(
        personalTrainerId: String,
        month: String,
    ): Result<Availability> {
        return Result.success(
            DataProvider.availability(personalTrainerId = personalTrainerId, month = month)
        )
    }

    private fun mockCheckAvailabilitySuccess(personalTrainerId: String): Result<CheckAvailability> {
        return Result.success(
            CheckAvailability(personalTrainerId = personalTrainerId, isAvailable = true)
        )
    }
}
