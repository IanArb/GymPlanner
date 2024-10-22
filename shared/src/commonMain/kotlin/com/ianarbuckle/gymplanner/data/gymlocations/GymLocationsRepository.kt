package com.ianarbuckle.gymplanner.data.gymlocations

import com.ianarbuckle.gymplanner.mapper.GymLocationsMapper.transformToGymLocations
import com.ianarbuckle.gymplanner.model.GymLocations

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