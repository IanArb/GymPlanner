package com.ianarbuckle.gymplanner.chat

import com.ianarbuckle.gymplanner.chat.ChatTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.chat.ChatTestDataProvider.Messages
import com.ianarbuckle.gymplanner.chat.ChatTestDataProvider.SessionData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ChatRepositoryTest {

    private lateinit var repository: ChatRepository
    private lateinit var fakeChatSocketService: FakeChatSocketService

    @BeforeTest
    fun setup() {
        fakeChatSocketService = FakeChatSocketService()
        repository = FakeChatRepository(fakeChatSocketService)
    }

    @AfterTest
    fun tearDown() {
        fakeChatSocketService.reset()
    }

    // ========== Init Session Tests ==========

    @Test
    fun `initSession with valid credentials returns success`() = runTest {
        // Given
        fakeChatSocketService.initSessionResult = Result.success(Unit)

        // When
        val result = repository.initSession(SessionData.username1, SessionData.userId1)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(Unit, result.getOrNull())
        assertEquals(1, fakeChatSocketService.initSessionCalls.size)
    }

    @Test
    fun `initSession calls socket service with correct parameters`() = runTest {
        // When
        repository.initSession(SessionData.username1, SessionData.userId1)

        // Then
        assertEquals(1, fakeChatSocketService.initSessionCalls.size)
        val (username, userId) = fakeChatSocketService.initSessionCalls[0]
        assertEquals(SessionData.username1, username)
        assertEquals(SessionData.userId1, userId)
    }

    @Test
    fun `initSession with different users works independently`() = runTest {
        // When
        val result1 = repository.initSession(SessionData.username1, SessionData.userId1)
        val result2 = repository.initSession(SessionData.username2, SessionData.userId2)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(2, fakeChatSocketService.initSessionCalls.size)
        assertEquals(SessionData.username1, fakeChatSocketService.initSessionCalls[0].first)
        assertEquals(SessionData.username2, fakeChatSocketService.initSessionCalls[1].first)
    }

    @Test
    fun `initSession with trainer credentials is handled`() = runTest {
        // When
        val result = repository.initSession(SessionData.trainerUsername, SessionData.trainerId)

        // Then
        assertTrue(result.isSuccess)
        val (username, userId) = fakeChatSocketService.initSessionCalls[0]
        assertEquals(SessionData.trainerUsername, username)
        assertEquals(SessionData.trainerId, userId)
    }

    @Test
    fun `initSession with network error returns failure`() = runTest {
        // Given
        fakeChatSocketService.shouldThrowExceptionOnInitSession = true
        fakeChatSocketService.initSessionException = Exceptions.networkError

        // When
        val result = repository.initSession(SessionData.username1, SessionData.userId1)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `initSession with unauthorized error returns failure`() = runTest {
        // Given
        fakeChatSocketService.shouldThrowExceptionOnInitSession = true
        fakeChatSocketService.initSessionException = Exceptions.unauthorized

        // When
        val result = repository.initSession(SessionData.username1, SessionData.userId1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `initSession with server error returns failure`() = runTest {
        // Given
        fakeChatSocketService.shouldThrowExceptionOnInitSession = true
        fakeChatSocketService.initSessionException = Exceptions.serverError

        // When
        val result = repository.initSession(SessionData.username1, SessionData.userId1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `initSession with empty username is handled`() = runTest {
        // When
        val result = repository.initSession(SessionData.emptyUsername, SessionData.userId1)

        // Then
        assertEquals(1, fakeChatSocketService.initSessionCalls.size)
        assertEquals("", fakeChatSocketService.initSessionCalls[0].first)
    }

    @Test
    fun `initSession with empty userId is handled`() = runTest {
        // When
        val result = repository.initSession(SessionData.username1, SessionData.emptyUserId)

        // Then
        assertEquals(1, fakeChatSocketService.initSessionCalls.size)
        assertEquals("", fakeChatSocketService.initSessionCalls[0].second)
    }

    // ========== Send Message Tests ==========

    @Test
    fun `sendMessage with valid message returns success`() = runTest {
        // Given
        fakeChatSocketService.sendMessageResult = Result.success(Unit)

        // When
        val result = repository.sendMessage(Messages.message1)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(Unit, result.getOrNull())
        assertEquals(1, fakeChatSocketService.sendMessageCalls.size)
    }

    @Test
    fun `sendMessage calls socket service with message text`() = runTest {
        // When
        repository.sendMessage(Messages.message1)

        // Then
        assertEquals(1, fakeChatSocketService.sendMessageCalls.size)
        assertEquals(Messages.message1.text, fakeChatSocketService.sendMessageCalls[0])
    }

    @Test
    fun `sendMessage extracts text from domain message correctly`() = runTest {
        // When
        repository.sendMessage(Messages.messageToSend)

        // Then
        val sentText = fakeChatSocketService.sendMessageCalls[0]
        assertEquals("Thank you for the advice!", sentText)
    }

    @Test
    fun `sendMessage with network error returns failure`() = runTest {
        // Given
        fakeChatSocketService.shouldThrowExceptionOnSendMessage = true
        fakeChatSocketService.sendMessageException = Exceptions.networkError

        // When
        val result = repository.sendMessage(Messages.message1)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `sendMessage with message not sent error returns failure`() = runTest {
        // Given
        fakeChatSocketService.shouldThrowExceptionOnSendMessage = true
        fakeChatSocketService.sendMessageException = Exceptions.messageNotSent

        // When
        val result = repository.sendMessage(Messages.message1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.messageNotSent, result.exceptionOrNull())
    }

    @Test
    fun `sendMessage with timeout error returns failure`() = runTest {
        // Given
        fakeChatSocketService.shouldThrowExceptionOnSendMessage = true
        fakeChatSocketService.sendMessageException = Exceptions.timeout

        // When
        val result = repository.sendMessage(Messages.message1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.timeout, result.exceptionOrNull())
    }

    @Test
    fun `multiple sendMessage calls work independently`() = runTest {
        // When
        val result1 = repository.sendMessage(Messages.message1)
        val result2 = repository.sendMessage(Messages.message2)
        val result3 = repository.sendMessage(Messages.message3)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertEquals(3, fakeChatSocketService.sendMessageCalls.size)
    }

    @Test
    fun `sendMessage with empty text is handled`() = runTest {
        // When
        val result = repository.sendMessage(Messages.emptyTextMessage)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("", fakeChatSocketService.sendMessageCalls[0])
    }

    @Test
    fun `sendMessage with long text is handled`() = runTest {
        // When
        val result = repository.sendMessage(Messages.longMessage)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(fakeChatSocketService.sendMessageCalls[0].length > 100)
    }

    // ========== Observe Messages Tests ==========

    @Test
    fun `observeMessages returns flow from socket service`() = runTest {
        // Given
        val expectedFlow = flowOf(Messages.message1, Messages.message2)
        fakeChatSocketService.observeMessagesFlow = expectedFlow

        // When
        val flow = repository.observeMessages()

        // Then
        assertNotNull(flow)
        val messages = flow.toList()
        assertEquals(2, messages.size)
        assertEquals(Messages.message1, messages[0])
        assertEquals(Messages.message2, messages[1])
    }

    @Test
    fun `observeMessages returns empty flow when no messages`() = runTest {
        // Given
        fakeChatSocketService.observeMessagesFlow = flowOf()

        // When
        val flow = repository.observeMessages()

        // Then
        val messages = flow.toList()
        assertTrue(messages.isEmpty())
    }

    @Test
    fun `observeMessages returns single message flow`() = runTest {
        // Given
        fakeChatSocketService.observeMessagesFlow = flowOf(Messages.message1)

        // When
        val flow = repository.observeMessages()

        // Then
        val message = flow.first()
        assertEquals(Messages.message1, message)
    }

    @Test
    fun `observeMessages preserves message order`() = runTest {
        // Given
        val messages = listOf(Messages.message1, Messages.message2, Messages.message3)
        fakeChatSocketService.observeMessagesFlow = flowOf(*messages.toTypedArray())

        // When
        val flow = repository.observeMessages()

        // Then
        val receivedMessages = flow.toList()
        assertEquals(3, receivedMessages.size)
        assertEquals(Messages.message1, receivedMessages[0])
        assertEquals(Messages.message2, receivedMessages[1])
        assertEquals(Messages.message3, receivedMessages[2])
    }

    @Test
    fun `observeMessages with messages from different users`() = runTest {
        // Given
        val messages = listOf(Messages.message1, Messages.messageFromJane, Messages.message2)
        fakeChatSocketService.observeMessagesFlow = flowOf(*messages.toTypedArray())

        // When
        val flow = repository.observeMessages()

        // Then
        val receivedMessages = flow.toList()
        assertEquals(3, receivedMessages.size)
        assertEquals("john_doe", receivedMessages[0].username)
        assertEquals("jane_smith", receivedMessages[1].username)
        assertEquals("trainer_sarah", receivedMessages[2].username)
    }

    // ========== Close Session Tests ==========

    @Test
    fun `closeSession calls socket service`() = runTest {
        // When
        repository.closeSession()

        // Then
        assertEquals(1, fakeChatSocketService.closeSessionCalls.size)
    }

    @Test
    fun `closeSession does not throw exception on success`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.closeSession()
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    @Test
    fun `multiple closeSession calls are handled`() = runTest {
        // When
        repository.closeSession()
        repository.closeSession()

        // Then
        assertEquals(2, fakeChatSocketService.closeSessionCalls.size)
    }

    // ========== Integration Tests ==========

    @Test
    fun `initSession followed by sendMessage works correctly`() = runTest {
        // When
        val initResult = repository.initSession(SessionData.username1, SessionData.userId1)
        val sendResult = repository.sendMessage(Messages.message1)

        // Then
        assertTrue(initResult.isSuccess)
        assertTrue(sendResult.isSuccess)
        assertEquals(1, fakeChatSocketService.initSessionCalls.size)
        assertEquals(1, fakeChatSocketService.sendMessageCalls.size)
    }

    @Test
    fun `initSession followed by observeMessages works correctly`() = runTest {
        // Given
        fakeChatSocketService.observeMessagesFlow = flowOf(Messages.message1)

        // When
        val initResult = repository.initSession(SessionData.username1, SessionData.userId1)
        val flow = repository.observeMessages()

        // Then
        assertTrue(initResult.isSuccess)
        val message = flow.first()
        assertEquals(Messages.message1, message)
    }

    @Test
    fun `full chat session lifecycle works correctly`() = runTest {
        // Given
        fakeChatSocketService.observeMessagesFlow = flowOf(Messages.message1, Messages.message2)

        // When
        val initResult = repository.initSession(SessionData.username1, SessionData.userId1)
        val sendResult1 = repository.sendMessage(Messages.message1)
        val sendResult2 = repository.sendMessage(Messages.message2)
        val messages = repository.observeMessages().toList()
        repository.closeSession()

        // Then
        assertTrue(initResult.isSuccess)
        assertTrue(sendResult1.isSuccess)
        assertTrue(sendResult2.isSuccess)
        assertEquals(2, messages.size)
        assertEquals(1, fakeChatSocketService.initSessionCalls.size)
        assertEquals(2, fakeChatSocketService.sendMessageCalls.size)
        assertEquals(1, fakeChatSocketService.closeSessionCalls.size)
    }

    @Test
    fun `sendMessage after closeSession still works`() = runTest {
        // When
        repository.closeSession()
        val result = repository.sendMessage(Messages.message1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, fakeChatSocketService.closeSessionCalls.size)
        assertEquals(1, fakeChatSocketService.sendMessageCalls.size)
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `initSession handles generic exceptions gracefully`() = runTest {
        // Given
        fakeChatSocketService.shouldThrowExceptionOnInitSession = true
        fakeChatSocketService.initSessionException = Exceptions.invalidMessage

        // When
        val result = repository.initSession(SessionData.username1, SessionData.userId1)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `sendMessage handles generic exceptions gracefully`() = runTest {
        // Given
        fakeChatSocketService.shouldThrowExceptionOnSendMessage = true
        fakeChatSocketService.sendMessageException = RuntimeException("Unexpected error")

        // When
        val result = repository.sendMessage(Messages.message1)

        // Then
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `successful initSession does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.initSession(SessionData.username1, SessionData.userId1)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    @Test
    fun `successful sendMessage does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.sendMessage(Messages.message1)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }
}

