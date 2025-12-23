package com.ianarbuckle.gymplanner.profile

import com.ianarbuckle.gymplanner.profile.domain.Profile

/**
 * Fake implementation of ProfileRepository for testing
 */
class FakeProfileRepository(
    private val remoteDataSource: ProfileRemoteDataSource
) {

    suspend fun getProfile(userId: String): Result<Profile> {
        return try {
            val profileDto = remoteDataSource.fetchProfile(userId)
            Result.success(profileDto.toProfile())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}

