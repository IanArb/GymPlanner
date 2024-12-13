package com.ianarbuckle.gymplanner.availability

import com.ianarbuckle.gymplanner.availability.dto.AvailabilityDto
import com.ianarbuckle.gymplanner.availability.dto.CheckAvailabilityDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class AvailabilityRemoteDataSource(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val dataStoreRepository: DataStoreRepository,
) {

    suspend fun fetchAvailability(personalTrainerId: String, month: String): AvailabilityDto {
        val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY)
        val url = baseUrl.plus(AVAILABILITY_ENDPOINT).plus("/$personalTrainerId/$month")
        val response = httpClient.get(url) {
            headers {
                append("Authorization", "Bearer $token")
            }
        }

        return response.body()
    }

    suspend fun checkAvailability(personalTrainerId: String, month: String): CheckAvailabilityDto {
        val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY)
        val url = baseUrl.plus(AVAILABILITY_ENDPOINT).plus("/check_availability")
        val response = httpClient.get(url) {
            parameter("personalTrainerId", personalTrainerId)
            parameter("month", month)
            headers {
                append("Authorization", "Bearer $token")
            }
        }

        return response.body()
    }

    private companion object {
        const val AVAILABILITY_ENDPOINT = "/api/v1/availability"
    }

}