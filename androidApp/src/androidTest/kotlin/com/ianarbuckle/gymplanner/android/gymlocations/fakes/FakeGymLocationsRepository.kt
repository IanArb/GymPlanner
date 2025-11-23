package com.ianarbuckle.gymplanner.android.gymlocations.fakes

import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import kotlinx.collections.immutable.ImmutableList

class FakeGymLocationsRepository : GymLocationsRepository {

    var shouldReturnError = false

    override suspend fun fetchGymLocations(): Result<ImmutableList<GymLocations>> {
        return if (shouldReturnError) {
            Result.failure(Exception("Error"))
        } else {
            mockGymLocationsSuccess()
        }
    }

    private fun mockGymLocationsSuccess(): Result<ImmutableList<GymLocations>> {
        return Result.success(DataProvider.gymLocations())
    }
}
