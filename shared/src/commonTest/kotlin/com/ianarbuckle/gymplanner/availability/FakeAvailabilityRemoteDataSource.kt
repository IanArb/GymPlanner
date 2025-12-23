package com.ianarbuckle.gymplanner.availability

import com.ianarbuckle.gymplanner.availability.dto.AvailabilityDto
import com.ianarbuckle.gymplanner.availability.dto.CheckAvailabilityDto

/**
 * Fake implementation for testing AvailabilityRepository
 * Implements the AvailabilityRemoteDataSource interface
 */
class FakeAvailabilityRemoteDataSource : AvailabilityRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnFetchAvailability = false
    var shouldThrowExceptionOnCheckAvailability = false
    var fetchAvailabilityException: Exception? = null
    var checkAvailabilityException: Exception? = null

    // Captured calls for verification
    val fetchAvailabilityCalls = mutableListOf<Pair<String, String>>()
    val checkAvailabilityCalls = mutableListOf<Pair<String, String>>()

    // Configurable responses
    var fetchAvailabilityResponse: AvailabilityDto = AvailabilityTestDataProvider.AvailabilityDtos.januaryWithSlots

    var checkAvailabilityResponse: CheckAvailabilityDto = AvailabilityTestDataProvider.CheckAvailabilityDtos.available

    override suspend fun fetchAvailability(personalTrainerId: String, month: String): AvailabilityDto {
        fetchAvailabilityCalls.add(Pair(personalTrainerId, month))

        if (shouldThrowExceptionOnFetchAvailability) {
            throw fetchAvailabilityException ?: RuntimeException("Fetch availability failed")
        }

        return fetchAvailabilityResponse
    }

    override suspend fun checkAvailability(personalTrainerId: String, month: String): CheckAvailabilityDto {
        checkAvailabilityCalls.add(Pair(personalTrainerId, month))

        if (shouldThrowExceptionOnCheckAvailability) {
            throw checkAvailabilityException ?: RuntimeException("Check availability failed")
        }

        return checkAvailabilityResponse
    }

    fun reset() {
        shouldThrowExceptionOnFetchAvailability = false
        shouldThrowExceptionOnCheckAvailability = false
        fetchAvailabilityException = null
        checkAvailabilityException = null
        fetchAvailabilityCalls.clear()
        checkAvailabilityCalls.clear()

        fetchAvailabilityResponse = AvailabilityTestDataProvider.AvailabilityDtos.januaryWithSlots
        checkAvailabilityResponse = AvailabilityTestDataProvider.CheckAvailabilityDtos.available
    }
}

