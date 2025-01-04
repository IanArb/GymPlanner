package com.ianarbuckle.gymplanner.android.booking.fakes

import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability

class FakeAvailabilityRepository : AvailabilityRepository {

    override suspend fun getAvailability(
        personalTrainerId: String,
        month: String,
    ): Result<Availability> {
        return mockAvailabilitySuccess(
            personalTrainerId = personalTrainerId,
            month = month,
        )
    }

    override suspend fun checkAvailability(
        personalTrainerId: String,
        month: String,
    ): Result<CheckAvailability> {
        return mockCheckAvailabilitySuccess(
            personalTrainerId = personalTrainerId,
        )
    }

    private fun mockAvailabilitySuccess(
        personalTrainerId: String,
        month: String,
    ): Result<Availability> {
        return Result.success(
            DataProvider.availability(
                personalTrainerId = personalTrainerId,
                month = month,
            ),
        )
    }

    private fun mockCheckAvailabilitySuccess(personalTrainerId: String): Result<CheckAvailability> {
        return Result.success(
            CheckAvailability(
                personalTrainerId = personalTrainerId,
                isAvailable = true,
            ),
        )
    }
}
