package com.ianarbuckle.gymplanner.chat

import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.coroutines.flow.Flow

/** Fake implementation of ChatRepository for testing */
class FakeChatRepository(private val chatSocketService: ChatSocketService) : ChatRepository {

    override suspend fun initSession(username: String, userId: String): Result<Unit> {
        return try {
            chatSocketService.initSession(username, userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendMessage(message: Message): Result<Unit> {
        return try {
            chatSocketService.sendMessage(message.text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeMessages(): Flow<Message> {
        return chatSocketService.observeMessages()
    }

    override suspend fun closeSession() {
        chatSocketService.closeSession()
    }
}
