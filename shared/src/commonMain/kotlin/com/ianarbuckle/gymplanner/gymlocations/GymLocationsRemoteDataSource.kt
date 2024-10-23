package com.ianarbuckle.gymplanner.gymlocations

import com.ianarbuckle.gymplanner.gymlocations.dto.GymLocationsDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path

class GymLocationsRemoteDataSource(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) {

    suspend fun gymLocations(): List<GymLocationsDto> {
        val response = httpClient.get(baseUrl) {
            url {
                protocol = URLProtocol.HTTPS
                path(ENDPOINT)
            }
        }

        return response.body()
    }

    companion object {
        private const val ENDPOINT = "/api/v1/gym-locations"
    }
}