package com.ianarbuckle.gymplanner.profile

import com.ianarbuckle.gymplanner.profile.dto.ProfileDto

/**
 * Fake implementation for testing ProfileRepository
 * Implements the ProfileRemoteDataSource interface
 */
class FakeProfileRemoteDataSource : ProfileRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnFetchProfile = false
    var fetchProfileException: Exception? = null

    // Captured calls for verification
    val fetchProfileCalls = mutableListOf<String>()

    // Configurable responses
    var fetchProfileResponse: ProfileDto = ProfileTestDataProvider.ProfileDtos.john

    override suspend fun fetchProfile(userId: String): ProfileDto {
        fetchProfileCalls.add(userId)

        if (shouldThrowExceptionOnFetchProfile) {
            throw fetchProfileException ?: RuntimeException("Fetch profile failed")
        }

        return fetchProfileResponse
    }

    fun reset() {
        shouldThrowExceptionOnFetchProfile = false
        fetchProfileException = null
        fetchProfileCalls.clear()

        fetchProfileResponse = ProfileTestDataProvider.ProfileDtos.john
    }
}

