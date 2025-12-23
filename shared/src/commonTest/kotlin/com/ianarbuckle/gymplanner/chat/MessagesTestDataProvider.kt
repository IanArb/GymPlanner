package com.ianarbuckle.gymplanner.chat

import com.ianarbuckle.gymplanner.chat.domain.Message
import com.ianarbuckle.gymplanner.chat.dto.MessageDto
import kotlinx.collections.immutable.persistentListOf

/**
 * Provides test data for Chat/Messages Repository tests
 */
object ChatTestDataProvider {

    // ========== Session Data ==========

    object SessionData {
        const val username1 = "john_doe"
        const val userId1 = "user-001"
        const val username2 = "jane_smith"
        const val userId2 = "user-002"
        const val trainerUsername = "trainer_sarah"
        const val trainerId = "trainer-001"
        const val emptyUsername = ""
        const val emptyUserId = ""
    }

    // ========== User IDs ==========

    object UserIds {
        const val user1 = "user-001"
        const val user2 = "user-002"
        const val user3 = "user-003"
        const val trainer1 = "trainer-001"
    }

    // ========== Usernames ==========

    object Usernames {
        const val john = "john_doe"
        const val jane = "jane_smith"
        const val mike = "mike_brown"
        const val trainerSarah = "trainer_sarah"
    }

    // ========== Timestamps ==========

    object Timestamps {
        const val morning = "2025-12-23T09:00:00Z"
        const val afternoon = "2025-12-23T14:30:00Z"
        const val evening = "2025-12-23T18:45:00Z"
        const val yesterday = "2025-12-22T10:00:00Z"
    }

    // ========== Message DTOs ==========

    object MessageDtos {
        val message1 = MessageDto(
            id = "msg-001",
            timestamp = Timestamps.morning,
            username = Usernames.john,
            userId = UserIds.user1,
            content = "Hello, I need help with my workout plan"
        )

        val message2 = MessageDto(
            id = "msg-002",
            timestamp = Timestamps.afternoon,
            username = Usernames.trainerSarah,
            userId = UserIds.trainer1,
            content = "Hi! I'd be happy to help. What are your goals?"
        )

        val message3 = MessageDto(
            id = "msg-003",
            timestamp = Timestamps.evening,
            username = Usernames.john,
            userId = UserIds.user1,
            content = "I want to build muscle and increase strength"
        )

        val messageFromJane = MessageDto(
            id = "msg-004",
            timestamp = Timestamps.morning,
            username = Usernames.jane,
            userId = UserIds.user2,
            content = "Can you recommend a good diet plan?"
        )

        val messageWithoutId = MessageDto(
            id = null,
            timestamp = Timestamps.afternoon,
            username = Usernames.mike,
            userId = UserIds.user3,
            content = "New message without ID"
        )
    }

    // ========== Messages (Domain) ==========

    object Messages {
        val message1 = Message(
            text = "Hello, I need help with my workout plan",
            username = Usernames.john,
            formattedTime = Timestamps.morning,
            userId = UserIds.user1
        )

        val message2 = Message(
            text = "Hi! I'd be happy to help. What are your goals?",
            username = Usernames.trainerSarah,
            formattedTime = Timestamps.afternoon,
            userId = UserIds.trainer1
        )

        val message3 = Message(
            text = "I want to build muscle and increase strength",
            username = Usernames.john,
            formattedTime = Timestamps.evening,
            userId = UserIds.user1
        )

        val messageFromJane = Message(
            text = "Can you recommend a good diet plan?",
            username = Usernames.jane,
            formattedTime = Timestamps.morning,
            userId = UserIds.user2
        )

        val messageToSend = Message(
            text = "Thank you for the advice!",
            username = Usernames.john,
            formattedTime = Timestamps.afternoon,
            userId = UserIds.user1
        )

        val emptyTextMessage = Message(
            text = "",
            username = Usernames.john,
            formattedTime = Timestamps.morning,
            userId = UserIds.user1
        )

        val longMessage = Message(
            text = "This is a very long message that contains a lot of text to test how the system handles longer messages with multiple sentences and detailed information about workout routines and fitness goals.",
            username = Usernames.mike,
            formattedTime = Timestamps.evening,
            userId = UserIds.user3
        )
    }

    // ========== Message Lists ==========

    object MessageLists {
        val conversation = listOf(
            MessageDtos.message1,
            MessageDtos.message2,
            MessageDtos.message3
        )

        val singleMessage = listOf(MessageDtos.message1)

        val emptyList = emptyList<MessageDto>()

        val multipleUsers = listOf(
            MessageDtos.message1,
            MessageDtos.messageFromJane,
            MessageDtos.message2
        )
    }

    object DomainMessageLists {
        val conversation = persistentListOf(
            Messages.message1,
            Messages.message2,
            Messages.message3
        )

        val singleMessage = persistentListOf(Messages.message1)

        val emptyList = persistentListOf<Message>()

        val multipleUsers = persistentListOf(
            Messages.message1,
            Messages.messageFromJane,
            Messages.message2
        )
    }

    // ========== Exceptions ==========

    object Exceptions {
        val networkError = Exception("Network unavailable")
        val serverError = RuntimeException("Internal server error")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val messageNotSent = RuntimeException("Failed to send message")
        val invalidMessage = IllegalArgumentException("Invalid message content")
        val timeout = Exception("Request timeout")
    }
}

