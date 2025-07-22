package com.ianarbuckle.gymplanner.android.chat.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ianarbuckle.gymplanner.android.chat.ChatScreenViewModel
import com.ianarbuckle.gymplanner.android.chat.ChatUiState
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import kotlinx.coroutines.delay

private const val BANNER_DELAY = 3000L

@Composable
fun ChatScreen(
    paddingValues: PaddingValues,
    username: String,
    modifier: Modifier = Modifier,
    chatScreenViewModel: ChatScreenViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                chatScreenViewModel.connect()
            } else if (event == Lifecycle.Event.ON_STOP) {
                chatScreenViewModel.disconnect()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer = observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            chatScreenViewModel.disconnect()
        }
    }

    var showBanner by remember { mutableStateOf(true) }

    LaunchedEffect(showBanner) {
        if (showBanner) {
            delay(BANNER_DELAY) // Show banner for 3 seconds
            showBanner = false
        }
    }

    val uiState = chatScreenViewModel.chatUiState.collectAsState()

    when (val state = uiState.value) {
        is ChatUiState.Loading -> {
            CircularProgressIndicator(modifier = modifier)
        }
        is ChatUiState.MessagesSuccess -> {
            val messageText = chatScreenViewModel.messageText.collectAsState()

            ChatScreenContent(
                messages = state.messages,
                username = username,
                onSendMessage = chatScreenViewModel::sendMessage,
                onMessageChange = chatScreenViewModel::onMessageChanged,
                messageText = messageText.value,
                modifier = modifier.padding(paddingValues).imePadding(),
            )
        }
        is ChatUiState.Failed -> {
            RetryErrorScreen(
                text = "Failed to load messages.",
                onClick = { chatScreenViewModel.connect() },
                modifier = modifier,
            )
        }
        ChatUiState.Idle -> {
            // Initial state, do nothing
        }
    }
}
