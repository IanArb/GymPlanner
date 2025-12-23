package com.ianarbuckle.gymplanner.gymlocations

import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations

/** Fake implementation of GymLocationsRepository for testing */
class FakeGymLocationsRepository(private val remoteDataSource: GymLocationsRemoteDataSource) {

    suspend fun getGymLocations(): Result<List<GymLocations>> {
        return try {
            val locations = remoteDataSource.gymLocations()
            val gymLocations = locations.map { it.transformToGymLocations() }
            Result.success(gymLocations)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
