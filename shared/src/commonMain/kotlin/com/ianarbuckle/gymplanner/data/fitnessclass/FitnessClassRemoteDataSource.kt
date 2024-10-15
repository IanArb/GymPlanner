package com.ianarbuckle.gymplanner.data.fitnessclass

import com.ianarbuckle.gymplanner.data.fitnessclass.dto.FitnessClassDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path

class FitnessClassRemoteDataSource(
    private val baseurl: String,
    private val httpClient: HttpClient
) {

    suspend fun fitnessClasses(dayOfWeek: String): List<FitnessClassDto> {
        val response = httpClient.get(baseurl) {
            url {
                protocol = URLProtocol.HTTPS
                path(ENDPOINT)
                parameters.append("dayOfWeek", dayOfWeek)
            }
        }

        return response.body()
    }

    companion object {
        private const val ENDPOINT = "/api/v1/fitnessclass"
    }
}