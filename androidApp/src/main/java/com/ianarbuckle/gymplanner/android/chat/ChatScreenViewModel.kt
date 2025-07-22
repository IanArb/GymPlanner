package com.ianarbuckle.gymplanner.android.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.chat.ChatRepository
import com.ianarbuckle.gymplanner.chat.MessagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ChatScreenViewModel
@Inject
constructor(
  private val chatRepository: ChatRepository,
  private val messagesRepository: MessagesRepository,
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val _chatUiState = MutableStateFlow<ChatUiState>(ChatUiState.Idle)
  val chatUiState = _chatUiState.asStateFlow()

  private val _messageText = MutableStateFlow("")
  val messageText = _messageText.asStateFlow()

  fun connect() {
    getAllMessages()

    val userId = savedStateHandle.get<String>("userId") ?: ""
    val username = savedStateHandle.get<String>("username") ?: "Guest"

    if (userId.isNotBlank()) {
      viewModelScope.launch {
        val result = chatRepository.initSession(username = username, userId = userId)

        result.fold(
          onSuccess = {
            chatRepository
              .observeMessages()
              .onEach { message ->
                _chatUiState.update { state ->
                  val currentMessages =
                    (state as? ChatUiState.MessagesSuccess)?.messages ?: emptyList()
                  val updatedMessages = currentMessages + message
                  Log.d("ChatScreenViewModel", "Updated messages: $updatedMessages")
                  ChatUiState.MessagesSuccess(messages = updatedMessages.toImmutableList())
                }
              }
              .launchIn(viewModelScope)
          },
          onFailure = {
            Log.e("ChatScreenViewModel", "Failed to connect: ${it.message}")
            _chatUiState.update { ChatUiState.Failed }
          },
        )
      }
    }
  }

  fun onMessageChanged(message: String) {
    _messageText.update { message }
  }

  fun sendMessage() {
    viewModelScope.launch {
      val message = _messageText.value
      if (message.isNotBlank()) {
        chatRepository
          .sendMessage(message)
          .fold(
            onSuccess = {
              _messageText.update { "" } // Clear the message input after sending
              Log.d("ChatScreenViewModel", "Message sent successfully: $message")
            },
            onFailure = {
              _messageText.update { "" }
              Log.e("ChatScreenViewModel", "Failed to send message: ${it.message}")
            },
          )
      }
    }
  }

  private fun getAllMessages() {
    viewModelScope.launch {
      _chatUiState.update { ChatUiState.Loading }

      val messages = messagesRepository.getMessages()

      messages.fold(
        onSuccess = { messages ->
          Log.d("ChatScreenViewModel", "Loaded messages: $messages")
          _chatUiState.update { ChatUiState.MessagesSuccess(messages = messages.toImmutableList()) }
        },
        onFailure = {
          _chatUiState.update { ChatUiState.Failed }
          Log.e("ChatScreenViewModel", "Failed to load messages: ${it.message}")
        },
      )
    }
  }

  fun disconnect() {
    viewModelScope.launch { chatRepository.closeSession() }
  }
}
