package com.ianarbuckle.gymplanner.chat

import com.ianarbuckle.gymplanner.chat.dto.MessageDto

/**
 * Fake implementation for testing MessagesRepository Implements the MessagesRemoteDataSource
 * interface
 */
class FakeMessagesRemoteDataSource : MessagesRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnGetMessages = false
    var shouldThrowExceptionOnSendMessage = false
    var getMessagesException: Exception? = null
    var sendMessageException: Exception? = null

    // Captured calls for verification
    val getMessagesCalls = mutableListOf<Unit>()
    val sendMessageCalls = mutableListOf<MessageDto>()

    // Configurable responses
    var getMessagesResponse: List<MessageDto> = ChatTestDataProvider.MessageLists.conversation

    override suspend fun getMessages(): List<MessageDto> {
        getMessagesCalls.add(Unit)

        if (shouldThrowExceptionOnGetMessages) {
            throw getMessagesException ?: RuntimeException("Get messages failed")
        }

        return getMessagesResponse
    }

    override suspend fun sendMessage(message: MessageDto) {
        sendMessageCalls.add(message)

        if (shouldThrowExceptionOnSendMessage) {
            throw sendMessageException ?: RuntimeException("Send message failed")
        }

        // void method - no return
    }

    fun reset() {
        shouldThrowExceptionOnGetMessages = false
        shouldThrowExceptionOnSendMessage = false
        getMessagesException = null
        sendMessageException = null
        getMessagesCalls.clear()
        sendMessageCalls.clear()

        getMessagesResponse = ChatTestDataProvider.MessageLists.conversation
    }
}
