package com.ianarbuckle.gymplanner.android.chat

import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.collections.immutable.ImmutableList

sealed interface ChatUiState {

    data object Idle : ChatUiState

    data object Failed : ChatUiState

    data object Loading : ChatUiState

    data class Messages(
        val messages: ImmutableList<Message>,
        val hasFailedMessage: Boolean = false,
    ) : ChatUiState
}
