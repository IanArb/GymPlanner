package com.ianarbuckle.gymplanner.chat

import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MessagesRepositoryTest {

    private lateinit var repository: MessagesRepository
    private lateinit var fakeRemoteDataSource: FakeMessagesRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeMessagesRemoteDataSource()
        repository = FakeMessagesRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Get Messages Tests ==========

    @Test
    fun `getMessages with conversation returns success with list of messages`() = runTest {
        // Given
        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.conversation

        // When
        val result = repository.getMessages()

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(ChatTestDataProvider.DomainMessageLists.conversation, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.getMessagesCalls.size)
    }

    @Test
    fun `getMessages calls remote data source`() = runTest {
        // When
        repository.getMessages()

        // Then
        assertEquals(1, fakeRemoteDataSource.getMessagesCalls.size)
    }

    @Test
    fun `getMessages with multiple messages returns correct count`() = runTest {
        // Given
        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.conversation

        // When
        val result = repository.getMessages()

        // Then
        val messages = result.getOrNull()
        assertNotNull(messages)
        assertEquals(3, messages.size)
    }

    @Test
    fun `getMessages with single message returns list with one item`() = runTest {
        // Given
        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.singleMessage

        // When
        val result = repository.getMessages()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(ChatTestDataProvider.DomainMessageLists.singleMessage, result.getOrNull())
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `getMessages with no messages returns empty list`() = runTest {
        // Given
        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.emptyList

        // When
        val result = repository.getMessages()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(ChatTestDataProvider.DomainMessageLists.emptyList, result.getOrNull())
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getMessages with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnGetMessages = true
        fakeRemoteDataSource.getMessagesException = ChatTestDataProvider.Exceptions.networkError

        // When
        val result = repository.getMessages()

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(ChatTestDataProvider.Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `getMessages with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnGetMessages = true
        fakeRemoteDataSource.getMessagesException = ChatTestDataProvider.Exceptions.serverError

        // When
        val result = repository.getMessages()

        // Then
        assertTrue(result.isFailure)
        assertEquals(ChatTestDataProvider.Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `getMessages with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnGetMessages = true
        fakeRemoteDataSource.getMessagesException = ChatTestDataProvider.Exceptions.unauthorized

        // When
        val result = repository.getMessages()

        // Then
        assertTrue(result.isFailure)
        assertEquals(ChatTestDataProvider.Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `getMessages maps DTOs to domain models correctly`() = runTest {
        // Given
        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.conversation

        // When
        val result = repository.getMessages()

        // Then
        val messages = result.getOrNull()
        assertNotNull(messages)
        assertEquals(3, messages.size)
        assertEquals("Hello, I need help with my workout plan", messages[0].text)
        assertEquals("john_doe", messages[0].username)
        assertEquals("user-001", messages[0].userId)
    }

    @Test
    fun `getMessages with messages from multiple users is handled`() = runTest {
        // Given
        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.multipleUsers

        // When
        val result = repository.getMessages()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(ChatTestDataProvider.DomainMessageLists.multipleUsers, result.getOrNull())
        val messages = result.getOrNull()
        assertNotNull(messages)
        assertEquals(3, messages.size)
    }

    @Test
    fun `getMessages returns immutable list`() = runTest {
        // Given
        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.conversation

        // When
        val result = repository.getMessages()

        // Then
        val messages = result.getOrNull()
        assertNotNull(messages)
        assertTrue(messages::class.simpleName?.contains("Immutable") == true
            || messages::class.simpleName?.contains("Persistent") == true)
    }

    @Test
    fun `multiple getMessages calls work independently`() = runTest {
        // When
        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.conversation
        val result1 = repository.getMessages()

        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.singleMessage
        val result2 = repository.getMessages()

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(3, result1.getOrNull()?.size)
        assertEquals(1, result2.getOrNull()?.size)
        assertEquals(2, fakeRemoteDataSource.getMessagesCalls.size)
    }

    // ========== Send Message Tests ==========

    @Test
    fun `sendMessage with valid message returns success`() = runTest {
        // When
        val result = repository.sendMessage(ChatTestDataProvider.Messages.messageToSend)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(Unit, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.sendMessageCalls.size)
    }

    @Test
    fun `sendMessage calls remote data source with correct data`() = runTest {
        // When
        repository.sendMessage(ChatTestDataProvider.Messages.message1)

        // Then
        assertEquals(1, fakeRemoteDataSource.sendMessageCalls.size)
        val sentMessage = fakeRemoteDataSource.sendMessageCalls[0]
        assertEquals("Hello, I need help with my workout plan", sentMessage.content)
        assertEquals("john_doe", sentMessage.username)
        assertEquals("user-001", sentMessage.userId)
    }

    @Test
    fun `sendMessage converts domain message to DTO correctly`() = runTest {
        // When
        repository.sendMessage(ChatTestDataProvider.Messages.messageToSend)

        // Then
        val sentMessage = fakeRemoteDataSource.sendMessageCalls[0]
        assertEquals(ChatTestDataProvider.Messages.messageToSend.text, sentMessage.content)
        assertEquals(ChatTestDataProvider.Messages.messageToSend.username, sentMessage.username)
        assertEquals(ChatTestDataProvider.Messages.messageToSend.userId, sentMessage.userId)
        assertEquals(ChatTestDataProvider.Messages.messageToSend.formattedTime, sentMessage.timestamp)
    }

    @Test
    fun `sendMessage with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSendMessage = true
        fakeRemoteDataSource.sendMessageException = ChatTestDataProvider.Exceptions.networkError

        // When
        val result = repository.sendMessage(ChatTestDataProvider.Messages.message1)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(expected = ChatTestDataProvider.Exceptions.networkError, actual = result.exceptionOrNull())
    }

    @Test
    fun `sendMessage with message not sent error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSendMessage = true
        fakeRemoteDataSource.sendMessageException = ChatTestDataProvider.Exceptions.messageNotSent

        // When
        val result = repository.sendMessage(ChatTestDataProvider.Messages.message1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(ChatTestDataProvider.Exceptions.messageNotSent, result.exceptionOrNull())
    }

    @Test
    fun `sendMessage with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSendMessage = true
        fakeRemoteDataSource.sendMessageException = ChatTestDataProvider.Exceptions.unauthorized

        // When
        val result = repository.sendMessage(ChatTestDataProvider.Messages.message1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(ChatTestDataProvider.Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `sendMessage with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSendMessage = true
        fakeRemoteDataSource.sendMessageException = ChatTestDataProvider.Exceptions.serverError

        // When
        val result = repository.sendMessage(ChatTestDataProvider.Messages.message1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(ChatTestDataProvider.Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `multiple sendMessage calls work independently`() = runTest {
        // When
        val result1 = repository.sendMessage(ChatTestDataProvider.Messages.message1)
        val result2 = repository.sendMessage(ChatTestDataProvider.Messages.message2)
        val result3 = repository.sendMessage(ChatTestDataProvider.Messages.message3)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertEquals(3, fakeRemoteDataSource.sendMessageCalls.size)
    }

    @Test
    fun `sendMessage with empty text is handled`() = runTest {
        // When
        val result = repository.sendMessage(ChatTestDataProvider.Messages.emptyTextMessage)

        // Then
        assertTrue(result.isSuccess)
        val sentMessage = fakeRemoteDataSource.sendMessageCalls[0]
        assertEquals("", sentMessage.content)
    }

    @Test
    fun `sendMessage with long text is handled`() = runTest {
        // When
        val result = repository.sendMessage(ChatTestDataProvider.Messages.longMessage)

        // Then
        assertTrue(result.isSuccess)
        val sentMessage = fakeRemoteDataSource.sendMessageCalls[0]
        assertTrue(sentMessage.content.length > 100)
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `getMessages handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnGetMessages = true
        fakeRemoteDataSource.getMessagesException = ChatTestDataProvider.Exceptions.timeout

        // When
        val result = repository.getMessages()

        // Then
        assertTrue(result.isFailure)
        assertEquals(ChatTestDataProvider.Exceptions.timeout, result.exceptionOrNull())
    }

    @Test
    fun `sendMessage handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSendMessage = true
        fakeRemoteDataSource.sendMessageException = ChatTestDataProvider.Exceptions.invalidMessage

        // When
        val result = repository.sendMessage(ChatTestDataProvider.Messages.message1)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `successful getMessages does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.getMessages()
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
            repository.sendMessage(ChatTestDataProvider.Messages.message1)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    // ========== Edge Cases ==========

    @Test
    fun `getMessages response contains all message fields`() = runTest {
        // Given
        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.conversation

        // When
        val result = repository.getMessages()

        // Then
        val messages = result.getOrNull()
        assertNotNull(messages)
        messages.forEach { message ->
            assertNotNull(message.text)
            assertNotNull(message.username)
            assertNotNull(message.userId)
            assertNotNull(message.formattedTime)
        }
    }

    @Test
    fun `sendMessage preserves all message fields`() = runTest {
        // Given
        val originalMessage = ChatTestDataProvider.Messages.messageToSend

        // When
        repository.sendMessage(originalMessage)

        // Then
        val sentMessage = fakeRemoteDataSource.sendMessageCalls[0]
        assertEquals(originalMessage.text, sentMessage.content)
        assertEquals(originalMessage.username, sentMessage.username)
        assertEquals(originalMessage.userId, sentMessage.userId)
        assertEquals(originalMessage.formattedTime, sentMessage.timestamp)
    }

    @Test
    fun `getMessages followed by sendMessage works correctly`() = runTest {
        // When
        val getResult = repository.getMessages()
        val sendResult = repository.sendMessage(ChatTestDataProvider.Messages.messageToSend)

        // Then
        assertTrue(getResult.isSuccess)
        assertTrue(sendResult.isSuccess)
        assertEquals(1, fakeRemoteDataSource.getMessagesCalls.size)
        assertEquals(1, fakeRemoteDataSource.sendMessageCalls.size)
    }

    @Test
    fun `sendMessage with different users works independently`() = runTest {
        // When
        val result1 = repository.sendMessage(ChatTestDataProvider.Messages.message1)
        val result2 = repository.sendMessage(ChatTestDataProvider.Messages.messageFromJane)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(2, fakeRemoteDataSource.sendMessageCalls.size)
        assertEquals("user-001", fakeRemoteDataSource.sendMessageCalls[0].userId)
        assertEquals("user-002", fakeRemoteDataSource.sendMessageCalls[1].userId)
    }

    @Test
    fun `getMessages order is preserved`() = runTest {
        // Given
        fakeRemoteDataSource.getMessagesResponse = ChatTestDataProvider.MessageLists.conversation

        // When
        val result = repository.getMessages()

        // Then
        val messages = result.getOrNull()
        assertNotNull(messages)
        assertEquals("Hello, I need help with my workout plan", messages[0].text)
        assertEquals("Hi! I'd be happy to help. What are your goals?", messages[1].text)
        assertEquals("I want to build muscle and increase strength", messages[2].text)
    }
}

