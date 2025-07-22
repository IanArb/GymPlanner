package com.ianarbuckle.gymplanner.android.chat

import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.collections.immutable.ImmutableList

sealed interface ChatUiState {

  data object Idle : ChatUiState

  data object Failed : ChatUiState

  data object Loading : ChatUiState

  data class MessagesSuccess(val messages: ImmutableList<Message>) : ChatUiState
}
