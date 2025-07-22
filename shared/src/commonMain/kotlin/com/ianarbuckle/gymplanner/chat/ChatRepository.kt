package com.ianarbuckle.gymplanner.chat

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.chat.domain.Message
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface ChatRepository {

    suspend fun initSession(username: String, userId: String): Result<Unit>

    suspend fun sendMessage(message: String): Result<Unit>

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()
}

class DefaultChatRepository() : ChatRepository, KoinComponent {

    private val chatSocketService: ChatSocketService by inject()

    override suspend fun initSession(username: String, userId: String): Result<Unit> {
        return try {
            chatSocketService.initSession(username, userId)
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Logger.e("ChatSocketService", e)
            Result.failure(e)
        }
    }

    override suspend fun sendMessage(message: String): Result<Unit> {
        return chatSocketService.sendMessage(message)
    }

    override fun observeMessages() = chatSocketService.observeMessages()

    override suspend fun closeSession() {
        chatSocketService.closeSession()
    }
}
