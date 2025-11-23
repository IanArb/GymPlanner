package com.ianarbuckle.gymplanner.android.chat.fakes

import com.ianarbuckle.gymplanner.chat.ChatRepository
import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class FakeChatRepository : ChatRepository {

    var shouldReturnError = false

    override suspend fun initSession(username: String, userId: String): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(Exception("Error"))
        } else {
            return Result.success(Unit)
        }
    }

    override suspend fun sendMessage(message: Message): Result<Unit> {
        if (shouldReturnError) {
            return Result.failure(Exception("Error"))
        } else {
            return Result.success(Unit)
        }
    }

    override fun observeMessages(): Flow<Message> {
        return if (shouldReturnError) {
            emptyFlow()
        } else {
            flowOf(
                Message(
                    username = "Test User",
                    text = "Hello, this is a test message!",
                    userId = "test-user",
                    formattedTime = "2025-09-12T20:08:55.806Z",
                )
            )
        }
    }

    override suspend fun closeSession() {
        return
    }
}
