package com.ianarbuckle.gymplanner.chat

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.chat.domain.Message
import com.ianarbuckle.gymplanner.chat.dto.MessageDto
import io.ktor.utils.io.CancellationException
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface MessagesRepository {

    suspend fun getMessages(): Result<ImmutableList<Message>>

    suspend fun sendMessage(message: Message): Result<Unit>
}

class DefaultMessagesRepository : MessagesRepository, KoinComponent {

    private val messagesRemoteDataSource: MessagesRemoteDataSource by inject()

    override suspend fun getMessages(): Result<ImmutableList<Message>> {
        return try {
            val messages =
                messagesRemoteDataSource.getMessages().map { it.toMessage() }.toImmutableList()
            Logger.d { "MessagesRepository: getMessages() - messages: $messages" }
            Result.success(value = messages)
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Logger.e("MessagesRepository", e)
            return Result.failure(e)
        }
    }

    override suspend fun sendMessage(message: Message): Result<Unit> {
        return try {
            messagesRemoteDataSource.sendMessage(
                MessageDto(
                    content = message.text,
                    timestamp = message.formattedTime,
                    username = message.username,
                    userId = message.userId,
                )
            )
            Logger.d { "MessagesRepository: sendMessage() - $message" }
            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Logger.e("MessagesRepository", e)
            Result.failure(e)
        }
    }
}
