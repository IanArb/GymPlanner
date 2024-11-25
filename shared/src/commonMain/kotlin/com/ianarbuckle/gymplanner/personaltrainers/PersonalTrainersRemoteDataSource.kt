package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.clients.dto.PersonalTrainerDto
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.collections.immutable.ImmutableList

class PersonalTrainersRemoteDataSource(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val dataStoreRepository: DataStoreRepository,
) {

    suspend fun fetchPersonalTrainers(gymLocation: GymLocation): List<PersonalTrainerDto> {
        val authorisationToken = dataStoreRepository.getStringData(AUTH_TOKEN_KEY) ?: ""
        val response = httpClient.get(baseUrl) {
            url {
                protocol = URLProtocol.HTTPS
                path(ENDPOINT)
                parameters.append("gymLocation", gymLocation.name)
            }
            headers {
                append("Authorization", "Bearer $authorisationToken")
            }
        }

        return response.body()
    }

    private companion object {
        private const val ENDPOINT = "/api/v1/personal_trainers"
    }
}