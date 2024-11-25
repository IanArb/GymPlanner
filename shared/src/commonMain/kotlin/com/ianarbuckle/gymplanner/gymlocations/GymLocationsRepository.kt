package com.ianarbuckle.gymplanner.gymlocations

import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocationsMapper.transformToGymLocations
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet


class GymLocationsRepository(
    private val localDataSource: GymLocationsLocalDataSource,
    private val remoteDataSource: GymLocationsRemoteDataSource,
) {

    suspend fun fetchGymLocations(): Result<ImmutableList<GymLocations>> {
        try {
            val remoteGymLocations = remoteDataSource.gymLocations()
            remoteGymLocations.map { gymLocation ->
                localDataSource.saveGymLocation(gymLocation.transformToGymLocations())
            }
            val gymLocations = remoteGymLocations.map { it.transformToGymLocations() }.toImmutableList()
            return Result.success(gymLocations)
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }
}