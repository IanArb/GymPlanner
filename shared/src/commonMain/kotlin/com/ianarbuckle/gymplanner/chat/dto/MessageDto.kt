package com.ianarbuckle.gymplanner.chat.dto

import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String,
    val timestamp: Long,
    val username: String,
    val userId: String,
    val content: String,
) {
    @OptIn(ExperimentalTime::class) val instant = Instant.fromEpochMilliseconds(timestamp)
    @OptIn(ExperimentalTime::class)
    val dateTime: LocalDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val formattedTime = dateTime.toString()

    fun toMessage(): Message {
        return Message(text = content, username = username, formattedTime = formattedTime)
    }
}
