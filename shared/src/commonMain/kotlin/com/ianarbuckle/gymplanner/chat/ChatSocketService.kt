package com.ianarbuckle.gymplanner.chat

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.chat.domain.Message
import com.ianarbuckle.gymplanner.chat.dto.MessageDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

interface ChatSocketService {

    suspend fun initSession(username: String, userId: String): Result<Unit>

    suspend fun sendMessage(message: String): Result<Unit>

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()
}

class ChatSocketServiceImpl(private val httpClient: HttpClient, private val baseUrl: String) :
    ChatSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(username: String, userId: String): Result<Unit> {
        return try {
            socket =
                httpClient.webSocketSession {
                    url("$baseUrl/$ENDPOINT")
                    parameter("username", username)
                    parameter("userId", userId)
                }

            if (socket?.isActive == true) {
                return Result.success(Unit)
            } else {
                Logger.e("ChatSocketService")
                Result.failure(Exception("Failed to establish WebSocket session"))
            }
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.e("ChatSocketService", ex)
            Result.failure(ex)
        }
    }

    override suspend fun sendMessage(message: String): Result<Unit> {
        try {
            socket?.send(Frame.Text(message))
            return Result.success(Unit)
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.e("ChatSocketService", ex)
            ex.printStackTrace()
            return Result.failure(ex)
        }
    }

    override fun observeMessages(): Flow<Message> {
        return try {
            socket
                ?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageDto = Json.decodeFromString<MessageDto>(json)
                    messageDto.toMessage()
                } ?: emptyFlow()
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.e("ChatSocketService", ex)
            emptyFlow()
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }

    companion object {
        private const val ENDPOINT = "conversation-socket"
    }
}
