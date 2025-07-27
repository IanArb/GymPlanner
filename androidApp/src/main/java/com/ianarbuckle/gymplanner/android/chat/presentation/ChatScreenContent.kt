package com.ianarbuckle.gymplanner.android.chat.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.chat.domain.Message
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenContent(
    messages: ImmutableList<Message>,
    username: String,
    messageText: String,
    onSendMessage: () -> Unit,
    onMessageChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        coroutineScope.launch {
            if (messages.isNotEmpty()) {
                listState.scrollToItem(messages.lastIndex)
            }
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(state = listState, modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
            items(messages.size) { index ->
                val message = messages[index]
                MessageBubble(
                    message = message.text,
                    timestamp = message.formattedTime,
                    username = message.username,
                    isMyself = message.username == username,
                )
            }
        }
        Composer(
            onMessageChange = onMessageChange,
            onSendMessage = onSendMessage,
            isEnabled = true,
            composerText = messageText,
            modifier = Modifier.imePadding(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenContentPreview() {
    GymAppTheme {
        Surface {
            ChatScreenContent(
                messages =
                    listOf(
                            Message(
                                text = "Hello",
                                username = "Support",
                                formattedTime = "2023-10-01 12:34:56",
                                userId = "support_user_id",
                            ),
                            Message(
                                text = "Hey! I have a question about my last workout.",
                                username = "You",
                                formattedTime = "2023-10-01 12:34:56",
                                userId = "your_user_id",
                            ),
                            Message(
                                text = "Sure! What would you like to know?",
                                username = "Support",
                                formattedTime = "2023-10-01 12:34:56",
                                userId = "support_user_id",
                            ),
                            Message(
                                text = "I want to know how to improve my deadlift technique.",
                                username = "You",
                                formattedTime = "2023-10-01 12:34:56",
                                userId = "your_user_id",
                            ),
                            Message(
                                text = "When are you free?.",
                                username = "Support",
                                formattedTime = "2023-10-01 12:34:56",
                                userId = "support_user_id",
                            ),
                            Message(
                                text = "Now. I'm here at the gym.",
                                username = "You",
                                formattedTime = "2023-10-01 12:34:56",
                                userId = "your_user_id",
                            ),
                            Message(
                                text = "Sweet! let's go to the deadlift platform.",
                                username = "Support",
                                formattedTime = "2023-10-01 12:34:56",
                                userId = "support_user_id",
                            ),
                        )
                        .toImmutableList(),
                username = "You",
                onSendMessage = {},
                onMessageChange = {},
                messageText = "",
            )
        }
    }
}
