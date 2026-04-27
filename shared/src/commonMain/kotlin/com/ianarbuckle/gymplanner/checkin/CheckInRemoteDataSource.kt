package com.ianarbuckle.gymplanner.checkin

import com.ianarbuckle.gymplanner.checkin.dto.CheckInRequestDto
import com.ianarbuckle.gymplanner.checkin.dto.CheckInResponseDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface CheckInRemoteDataSource {
    suspend fun checkIn(trainerId: String, request: CheckInRequestDto): CheckInResponseDto
}

class DefaultCheckInRemoteDataSource(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val dataStoreRepository: DataStoreRepository,
) : CheckInRemoteDataSource {

    override suspend fun checkIn(
        trainerId: String,
        request: CheckInRequestDto,
    ): CheckInResponseDto {
        val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY)
        val response =
            httpClient.post(baseUrl.plus("$TRAINERS_ENDPOINT/$trainerId/$CHECK_IN_PATH")) {
                contentType(ContentType.Application.Json)
                setBody(request)
                headers { append("Authorization", "Bearer $token") }
            }
        return response.body()
    }

    companion object {
        const val TRAINERS_ENDPOINT = "/api/v1/trainers"
        const val CHECK_IN_PATH = "check-in"
    }
}
