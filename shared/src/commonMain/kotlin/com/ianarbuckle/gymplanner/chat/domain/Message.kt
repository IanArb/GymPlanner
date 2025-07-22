package com.ianarbuckle.gymplanner.chat.domain

import kotlinx.serialization.Serializable

@Serializable data class Message(val text: String, val username: String, val formattedTime: String)
