package com.ianarbuckle.gymplanner.profile

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.profile.domain.Profile
import com.ianarbuckle.gymplanner.profile.domain.ProfileMapper.toProfile
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CancellationException
import okio.IOException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface ProfileRepository {
    suspend fun fetchProfile(userId: String): Result<Profile>
}

class DefaultProfileRepository : ProfileRepository, KoinComponent {

    private val remoteDataSource: ProfileRemoteDataSource by inject()

    override suspend fun fetchProfile(userId: String): Result<Profile> {
        return try {
            val result = remoteDataSource.fetchProfile(userId)
            val profile = result.toProfile()
            Result.success(profile)
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.withTag("ProfileRepository").e("Error fetching profile: $ex")
            return Result.failure(ex)
        }
    }
}