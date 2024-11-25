package com.ianarbuckle.gymplanner.profile

import com.ianarbuckle.gymplanner.profile.domain.Profile
import com.ianarbuckle.gymplanner.profile.domain.ProfileMapper.toProfile

class ProfileRepository(private val remoteDataSource: ProfileRemoteDataSource) {

    suspend fun fetchProfile(userId: String): Result<Profile> {
        return try {
            val result = remoteDataSource.fetchProfile(userId)
            val profile = result.toProfile()
            Result.success(profile)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}