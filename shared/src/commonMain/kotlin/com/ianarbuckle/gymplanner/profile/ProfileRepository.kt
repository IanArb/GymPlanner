package com.ianarbuckle.gymplanner.profile

import com.ianarbuckle.gymplanner.profile.domain.Profile
import com.ianarbuckle.gymplanner.profile.domain.ProfileMapper.toProfile
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException

class ProfileRepository(private val remoteDataSource: ProfileRemoteDataSource) {

    suspend fun fetchProfile(userId: String): Result<Profile> {
        return try {
            val result = remoteDataSource.fetchProfile(userId)
            val profile = result.toProfile()
            Result.success(profile)
        } catch (ex: ClientRequestException) {
            return Result.failure(ex)
        }
        catch (ex: ServerResponseException) {
            return Result.failure(ex)
        }
        catch (ex: HttpRequestTimeoutException) {
            return Result.failure(ex)
        }
        catch (ex: ResponseException) {
            return Result.failure(ex)
        }
    }
}