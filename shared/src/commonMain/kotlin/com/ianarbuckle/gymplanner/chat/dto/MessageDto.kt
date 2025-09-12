package com.ianarbuckle.gymplanner.chat.dto

import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String? = null,
    val timestamp: String? = null,
    val username: String? = null,
    val userId: String? = null,
    val content: String? = null,
) {

    fun toMessage(): Message {
        return Message(
            text = content ?: "",
            username = username ?: "",
            userId = userId ?: "",
            formattedTime = timestamp ?: "",
        )
    }
}
