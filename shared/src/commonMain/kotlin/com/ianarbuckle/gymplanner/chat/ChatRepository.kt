package com.ianarbuckle.gymplanner.chat

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.chat.domain.Message
import com.ianarbuckle.gymplanner.chat.dto.MessageDto
import io.ktor.utils.io.CancellationException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface ChatRepository {

    suspend fun initSession(username: String, userId: String): Result<Unit>

    suspend fun sendMessage(message: Message): Result<Unit>

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

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun sendMessage(message: Message): Result<Unit> {
        val timeZone = TimeZone.currentSystemDefault()
        val message =
            MessageDto(
                content = message.text,
                timestamp = Clock.System.now().toLocalDateTime(timeZone).toString(),
                username = message.username,
                userId = message.userId,
            )

        return try {
            chatSocketService.sendMessage(message.content ?: "")
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Logger.e("ChatSocketService", e)
            Result.failure(e)
        }
    }

    override fun observeMessages() = chatSocketService.observeMessages()

    override suspend fun closeSession() {
        chatSocketService.closeSession()
    }
}
