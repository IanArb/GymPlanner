package com.ianarbuckle.gymplanner.gymlocations

import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocationsMapper.transformToGymLocations


class GymLocationsRepository(
    private val localDataSource: GymLocationsLocalDataSource,
    private val remoteDataSource: GymLocationsRemoteDataSource,
) {

    suspend fun fetchGymLocations(): Result<List<GymLocations>> {
        try {
            val remoteGymLocations = remoteDataSource.gymLocations()
            remoteGymLocations.map { gymLocation ->
                localDataSource.saveGymLocation(gymLocation.transformToGymLocations())
            }
            return Result.success(remoteGymLocations.map { it.transformToGymLocations() })
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }
}