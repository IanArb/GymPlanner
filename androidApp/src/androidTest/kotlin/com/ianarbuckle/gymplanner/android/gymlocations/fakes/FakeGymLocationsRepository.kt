package com.ianarbuckle.gymplanner.android.gymlocations.fakes

import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.common.ApiResult
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import kotlinx.collections.immutable.ImmutableList

class FakeGymLocationsRepository : GymLocationsRepository {
    var shouldReturnError = false

    override suspend fun fetchGymLocations(): ApiResult<ImmutableList<GymLocations>> = if (shouldReturnError) {
        ApiResult.Failure(Exception("Error"))
    } else {
        mockGymLocationsSuccess()
    }

    private fun mockGymLocationsSuccess(): ApiResult<ImmutableList<GymLocations>> = ApiResult.Success(DataProvider.gymLocations())
}
