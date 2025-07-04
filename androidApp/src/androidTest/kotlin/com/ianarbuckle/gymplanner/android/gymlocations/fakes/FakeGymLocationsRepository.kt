package com.ianarbuckle.gymplanner.android.gymlocations.fakes

import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import kotlinx.collections.immutable.ImmutableList

class FakeGymLocationsRepository : GymLocationsRepository {

  override suspend fun fetchGymLocations(): Result<ImmutableList<GymLocations>> {
    return mockGymLocationsSuccess()
  }

  private fun mockGymLocationsSuccess(): Result<ImmutableList<GymLocations>> {
    return Result.success(DataProvider.gymLocations())
  }
}
