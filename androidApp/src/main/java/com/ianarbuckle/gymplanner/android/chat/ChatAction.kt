package com.ianarbuckle.gymplanner.android.chat

sealed interface ChatAction {

    data object SendMessage : ChatAction

    data object Retry : ChatAction

    data class MessageChanged(val message: String) : ChatAction

    data object Reconnect : ChatAction
}
