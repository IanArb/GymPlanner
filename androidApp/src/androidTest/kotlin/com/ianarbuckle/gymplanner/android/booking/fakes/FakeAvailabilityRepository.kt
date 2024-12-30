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
        return mockAvailabilitySuccess()
    }

    override suspend fun checkAvailability(
        personalTrainerId: String,
        month: String,
    ): Result<CheckAvailability> {
        return mockCheckAvailabilitySuccess()
    }

    private fun mockAvailabilitySuccess(): Result<Availability> {
        return Result.success(
            DataProvider.availability(),
        )
    }

    private fun mockCheckAvailabilitySuccess(): Result<CheckAvailability> {
        return Result.success(
            CheckAvailability(
                personalTrainerId = "6730e1cb37f4352118e0c8e1",
                isAvailable = true,
            ),
        )
    }
}
