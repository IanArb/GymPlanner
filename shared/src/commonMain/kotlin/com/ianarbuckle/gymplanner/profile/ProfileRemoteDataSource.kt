package com.ianarbuckle.gymplanner.profile

import com.ianarbuckle.gymplanner.profile.dto.ProfileDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import io.ktor.http.path

interface ProfileRemoteDataSource {
    suspend fun fetchProfile(userId: String): ProfileDto
}

class DefaultProfileRemoteDataSource(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val dataStoreRepository: DataStoreRepository,
) : ProfileRemoteDataSource {

    override suspend fun fetchProfile(userId: String): ProfileDto {
        val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY) ?: ""
        val response =
            httpClient.get(baseUrl) {
                url {
                    protocol = URLProtocol.HTTPS
                    path(ENDPOINT.plus("/$userId"))
                }
                headers { append("Authorization", "Bearer $token") }
            }

        return response.body()
    }

    companion object Companion {
        private const val ENDPOINT = "/api/v1/user_profile"
    }
}
