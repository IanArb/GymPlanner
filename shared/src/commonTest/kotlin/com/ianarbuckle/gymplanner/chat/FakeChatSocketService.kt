package com.ianarbuckle.gymplanner.chat

import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/** Fake implementation for testing ChatRepository Implements the ChatSocketService interface */
class FakeChatSocketService : ChatSocketService {

    // Control flags for test scenarios
    var shouldThrowExceptionOnInitSession = false
    var shouldThrowExceptionOnSendMessage = false
    var shouldThrowExceptionOnCloseSession = false
    var initSessionException: Exception? = null
    var sendMessageException: Exception? = null
    var closeSessionException: Exception? = null

    // Captured calls for verification
    val initSessionCalls = mutableListOf<Pair<String, String>>()
    val sendMessageCalls = mutableListOf<String>()
    val closeSessionCalls = mutableListOf<Unit>()

    // Configurable responses
    var initSessionResult: Result<Unit> = Result.success(Unit)
    var sendMessageResult: Result<Unit> = Result.success(Unit)
    var observeMessagesFlow: Flow<Message> = emptyFlow()

    override suspend fun initSession(username: String, userId: String): Result<Unit> {
        initSessionCalls.add(Pair(username, userId))

        if (shouldThrowExceptionOnInitSession) {
            throw initSessionException ?: RuntimeException("Init session failed")
        }

        return initSessionResult
    }

    override suspend fun sendMessage(message: String): Result<Unit> {
        sendMessageCalls.add(message)

        if (shouldThrowExceptionOnSendMessage) {
            throw sendMessageException ?: RuntimeException("Send message failed")
        }

        return sendMessageResult
    }

    override fun observeMessages(): Flow<Message> {
        return observeMessagesFlow
    }

    override suspend fun closeSession() {
        closeSessionCalls.add(Unit)

        if (shouldThrowExceptionOnCloseSession) {
            throw closeSessionException ?: RuntimeException("Close session failed")
        }
    }

    fun reset() {
        shouldThrowExceptionOnInitSession = false
        shouldThrowExceptionOnSendMessage = false
        shouldThrowExceptionOnCloseSession = false
        initSessionException = null
        sendMessageException = null
        closeSessionException = null
        initSessionCalls.clear()
        sendMessageCalls.clear()
        closeSessionCalls.clear()

        initSessionResult = Result.success(Unit)
        sendMessageResult = Result.success(Unit)
        observeMessagesFlow = emptyFlow()
    }
}
