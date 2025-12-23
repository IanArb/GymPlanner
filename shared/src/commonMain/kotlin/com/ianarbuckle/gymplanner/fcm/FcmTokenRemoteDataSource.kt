package com.ianarbuckle.gymplanner.fcm

import com.ianarbuckle.gymplanner.fcm.domain.FcmTokenRequest
import com.ianarbuckle.gymplanner.fcm.dto.FcmTokenResponseDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface FcmTokenRemoteDataSource {
    suspend fun registerToken(fcmTokenRequest: FcmTokenRequest): FcmTokenResponseDto
}

class DefaultFcmTokenRemoteDataSource(
    private val baseurl: String,
    private val httpClient: HttpClient,
    private val dataStoreRepository: DataStoreRepository,
) : FcmTokenRemoteDataSource {

    override suspend fun registerToken(fcmTokenRequest: FcmTokenRequest): FcmTokenResponseDto {
        val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY) ?: ""
        val response =
            httpClient.post(baseurl.plus(ENDPOINT.plus("/register"))) {
                contentType(ContentType.Application.Json)
                setBody(fcmTokenRequest)
                headers { append("Authorization", "Bearer $token") }
            }
        return response.body()
    }

    companion object Companion {
        private const val ENDPOINT = "/api/v1/fcm"
    }
}
