package gymplanner.screenshots.chat

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.android.tools.screenshot.PreviewTest
import com.ianarbuckle.gymplanner.android.chat.presentation.ChatScreenContent
import com.ianarbuckle.gymplanner.android.chat.presentation.Composer
import com.ianarbuckle.gymplanner.android.chat.presentation.ConnectionBanner
import com.ianarbuckle.gymplanner.android.chat.presentation.MessageBubble
import com.ianarbuckle.gymplanner.chat.domain.Message
import gymplanner.screenshots.ScreenshotPreview
import gymplanner.screenshots.ScreenshotPreviews
import kotlinx.collections.immutable.persistentListOf

private const val MessageText = "Hello, this is a test message!"
private const val Timestamp = "2025-09-12T20:08:55.806Z"

@PreviewTest
@ScreenshotPreviews
@Composable
private fun OwnMessageBubbleScreenshot() {
    ScreenshotPreview {
        MessageBubble(
            message = MessageText,
            timestamp = Timestamp,
            username = "User1",
            isMyself = true,
            isFailedMessage = false,
        )
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
private fun OtherMessageBubbleScreenshot() {
    ScreenshotPreview {
        MessageBubble(
            message = MessageText,
            timestamp = Timestamp,
            username = "User1",
            isMyself = false,
            isFailedMessage = false,
        )
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
private fun ComposerScreenshot() {
    ScreenshotPreview { Composer(onSendMessage = {}, onMessageChange = {}, isEnabled = true) }
}

@PreviewTest
@ScreenshotPreviews
@Composable
private fun ConnectionBannerScreenshot() {
    ScreenshotPreview {
        ConnectionBanner(
            visible = true,
            connectionText = "Connecting...",
            backgroundColor = Color.Black,
            textColor = Color.White,
            iconColor = Color.White,
        )
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
private fun ChatScreenContentScreenshot() {
    ScreenshotPreview {
        ChatScreenContent(
            username = "Ian",
            messages =
            persistentListOf(
                Message(
                    text = MessageText,
                    username = "Ian",
                    userId = "testUserId",
                    formattedTime = Timestamp,
                ),
                Message(
                    text = "Hello!",
                    username = "Jane",
                    userId = "anotherUserId",
                    formattedTime = "2025-09-12T20:09:55.806Z",
                ),
            ),
            messageText = "Send message",
            onSendMessage = {},
            onMessageChange = {},
        )
    }
}
