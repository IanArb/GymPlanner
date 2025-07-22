package com.ianarbuckle.gymplanner.android.chat.fakes

import com.ianarbuckle.gymplanner.chat.ChatRepository
import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeChatRepository : ChatRepository {

  override suspend fun initSession(username: String, userId: String): Result<Unit> {
    return Result.success(Unit)
  }

  override suspend fun sendMessage(message: String): Result<Unit> {
    return Result.success(Unit)
  }

  override fun observeMessages(): Flow<Message> {
    return flowOf(
      Message(
        username = "Test User",
        text = "Hello, this is a test message!",
        formattedTime = "2025-10-01 12:34:56",
      )
    )
  }

  override suspend fun closeSession() {
    return
  }
}
