package com.ianarbuckle.gymplanner.fitnessclass

import com.ianarbuckle.gymplanner.fitnessclass.dto.FitnessClassDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import io.ktor.http.path

interface FitnessClassRemoteDataSource {
    suspend fun fitnessClasses(dayOfWeek: String): List<FitnessClassDto>
}

class DefaultFitnessClassRemoteDataSource(
    private val baseurl: String,
    private val httpClient: HttpClient,
    private val dataStoreRepository: DataStoreRepository,
): FitnessClassRemoteDataSource {

    override suspend fun fitnessClasses(dayOfWeek: String): List<FitnessClassDto> {
        val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY) ?: ""
        val response =
            httpClient.get(baseurl) {
                url {
                    protocol = URLProtocol.HTTPS
                    path(ENDPOINT)
                    parameters.append("dayOfWeek", dayOfWeek)
                }
                headers { append("Authorization", "Bearer $token") }
            }

        return response.body()
    }

    companion object Companion {
        private const val ENDPOINT = "/api/v1/fitness_class"
    }
}
