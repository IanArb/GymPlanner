package com.ianarbuckle.gymplanner.android.chat.fakes

import com.ianarbuckle.gymplanner.chat.ChatRepository
import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeChatRepository : ChatRepository {

    override suspend fun initSession(username: String, userId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun sendMessage(message: Message): Result<Unit> {
        return Result.success(Unit)
    }

    override fun observeMessages(): Flow<Message> {
        return flowOf(
            Message(
                username = "Test User",
                text = "Hello, this is a test message!",
                userId = "test-user",
                formattedTime = "2025-09-12T20:08:55.806Z",
            )
        )
    }

    override suspend fun closeSession() {
        return
    }
}
