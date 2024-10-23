package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.clients.dto.PersonalTrainerDto
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path

class PersonalTrainersRemoteDataSource(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {

    suspend fun fetchPersonalTrainers(gymLocation: GymLocation): List<PersonalTrainerDto> {
        val response = httpClient.get(baseUrl) {
            url {
                protocol = URLProtocol.HTTPS
                path(ENDPOINT)
                parameters.append("gymLocation", gymLocation.name)
            }
        }

        return response.body()
    }

    private companion object {
        private const val ENDPOINT = "/api/v1/personaltrainers"
    }
}