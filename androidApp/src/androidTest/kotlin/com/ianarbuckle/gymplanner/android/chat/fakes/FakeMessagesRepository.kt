package com.ianarbuckle.gymplanner.android.chat.fakes

import com.ianarbuckle.gymplanner.chat.MessagesRepository
import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class FakeMessagesRepository : MessagesRepository {

    override suspend fun getMessages(): Result<ImmutableList<Message>> {
        return Result.success(
            persistentListOf(
                Message(
                    username = "First User",
                    userId = "user-1",
                    text = "Hello, this is a first test message!",
                    formattedTime = "2025-09-12T20:08:55.806Z",
                ),
                Message(
                    username = "Second User",
                    userId = "user-2",
                    text = "Hello, this is a second test message!",
                    formattedTime = "2025-09-12T20:09:55.806Z",
                ),
            )
        )
    }

    override suspend fun sendMessage(message: Message): Result<Unit> {
        return Result.success(Unit)
    }
}
