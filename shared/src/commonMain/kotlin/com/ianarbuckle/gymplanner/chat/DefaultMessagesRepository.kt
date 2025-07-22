package com.ianarbuckle.gymplanner.chat

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.chat.domain.Message
import io.ktor.utils.io.CancellationException
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface MessagesRepository {

    suspend fun getMessages(): Result<ImmutableList<Message>>
}

class DefaultMessagesRepository() : MessagesRepository, KoinComponent {

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
}
