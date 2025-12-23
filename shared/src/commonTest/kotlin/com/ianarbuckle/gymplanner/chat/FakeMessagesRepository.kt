package com.ianarbuckle.gymplanner.chat

import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Fake implementation of MessagesRepository for testing
 */
class FakeMessagesRepository(
    private val remoteDataSource: MessagesRemoteDataSource
) : MessagesRepository {

    override suspend fun getMessages(): Result<ImmutableList<Message>> {
        return try {
            val messages = remoteDataSource.getMessages()
                .map { it.toMessage() }
                .toImmutableList()
            Result.success(messages)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun sendMessage(message: Message): Result<Unit> {
        return try {
            remoteDataSource.sendMessage(message.toMessageDto())
            Result.success(Unit)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}

