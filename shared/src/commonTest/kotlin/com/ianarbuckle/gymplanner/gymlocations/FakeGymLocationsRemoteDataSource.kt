package com.ianarbuckle.gymplanner.gymlocations

import com.ianarbuckle.gymplanner.gymlocations.dto.GymLocationsDto

/**
 * Fake implementation for testing GymLocationsRepository Implements the
 * GymLocationsRemoteDataSource interface
 */
class FakeGymLocationsRemoteDataSource : GymLocationsRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnGymLocations = false
    var gymLocationsException: Exception? = null

    // Captured calls for verification
    val gymLocationsCalls = mutableListOf<Unit>()

    // Configurable responses
    var gymLocationsResponse: List<GymLocationsDto> =
        GymLocationsTestDataProvider.GymLocationLists.allLocations

    override suspend fun gymLocations(): List<GymLocationsDto> {
        gymLocationsCalls.add(Unit)

        if (shouldThrowExceptionOnGymLocations) {
            throw gymLocationsException ?: RuntimeException("Fetch gym locations failed")
        }

        return gymLocationsResponse
    }

    fun reset() {
        shouldThrowExceptionOnGymLocations = false
        gymLocationsException = null
        gymLocationsCalls.clear()

        gymLocationsResponse = GymLocationsTestDataProvider.GymLocationLists.allLocations
    }
}
