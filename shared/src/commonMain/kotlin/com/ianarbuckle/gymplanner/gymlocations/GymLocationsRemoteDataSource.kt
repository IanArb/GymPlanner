package com.ianarbuckle.gymplanner.gymlocations

import com.ianarbuckle.gymplanner.gymlocations.dto.GymLocationsDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import io.ktor.http.path

class GymLocationsRemoteDataSource(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val dataStoreRepository: DataStoreRepository,
) {

    suspend fun gymLocations(): List<GymLocationsDto> {
        val authorisationToken = dataStoreRepository.getStringData(AUTH_TOKEN_KEY) ?: ""
        val response = httpClient.get(baseUrl) {
            url {
                protocol = URLProtocol.HTTPS
                path(ENDPOINT)
            }
            headers {
                append("Authorization", "Bearer $authorisationToken")
            }
        }

        return response.body()
    }

    companion object {
        private const val ENDPOINT = "/api/v1/gym_locations"
    }
}
