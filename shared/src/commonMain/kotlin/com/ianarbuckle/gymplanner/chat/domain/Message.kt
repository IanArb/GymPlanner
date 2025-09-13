package com.ianarbuckle.gymplanner.chat.domain

import com.ianarbuckle.gymplanner.chat.dto.MessageDto
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val text: String,
    val username: String,
    val formattedTime: String,
    val userId: String,
) {
    fun toMessageDto(): MessageDto {
        return MessageDto(
            content = text,
            username = username,
            userId = userId,
            timestamp = formattedTime,
        )
    }
}
