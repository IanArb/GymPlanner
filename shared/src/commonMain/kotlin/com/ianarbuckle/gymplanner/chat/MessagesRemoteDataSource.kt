package com.ianarbuckle.gymplanner.chat

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.chat.dto.MessageDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.utils.prettyPrintBody
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class MessagesRemoteDataSource(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val dataStoreRepository: DataStoreRepository,
    private val json: Json = Json { prettyPrint = true },
) {

    suspend fun getMessages(): List<MessageDto> {
        val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY)
        val url = baseUrl.plus(MESSAGES_ENDPOINT)
        val response = httpClient.get(url) { headers { append("Authorization", "Bearer $token") } }
        return try {
            val body = response.body<List<MessageDto>>()
            Logger.d {
                "MessagesRemoteDataSource: getMessages() " +
                    "- URL: $url, " +
                    "- Status: ${response.status.value}" +
                    " - Body: ${json.prettyPrintBody(body)}"
            }
            body
        } catch (e: Exception) {
            Logger.e("MessagesRemoteDataSource", e)
            emptyList()
        }
    }

    suspend fun sendMessage(message: MessageDto) {
        val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY)
        val url = baseUrl.plus(MESSAGES_ENDPOINT)
        try {
            val post =
                httpClient.post(url) {
                    headers { append("Authorization", "Bearer $token") }
                    contentType(ContentType.Application.Json)
                    setBody(message)
                }
            Logger.d {
                val body = post.body<MessageDto>()
                "MessagesRemoteDataSource: sendMessage() " +
                    "- ${post.status} " +
                    "+ ${json.prettyPrintBody(body)}"
            }
        } catch (e: Exception) {
            Logger.e("MessagesRemoteDataSource", e)
            throw e
        }
    }

    companion object {
        const val MESSAGES_ENDPOINT = "/api/v1/messages"
    }
}
