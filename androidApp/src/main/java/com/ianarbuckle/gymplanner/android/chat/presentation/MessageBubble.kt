package com.ianarbuckle.gymplanner.android.chat.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun MessageBubble(
    message: String,
    timestamp: String,
    username: String,
    isMyself: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = if (isMyself) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        if (isMyself) {
            TextMessageBubble(
                message = message,
                timestamp = timestamp,
                username = username,
                topLeft = 0.dp,
                topRight = 16.dp,
                bottomLeft = 16.dp,
                bottomRight = 16.dp,
                isMyself = true,
            )
        } else {
            TextMessageBubble(
                message = message,
                timestamp = timestamp,
                username = username,
                topLeft = 16.dp,
                topRight = 16.dp,
                bottomLeft = 16.dp,
                bottomRight = 0.dp,
                isMyself = false,
            )
        }
    }
}

@Composable
fun TextMessageBubble(
    message: String,
    timestamp: String,
    username: String,
    topLeft: Dp,
    topRight: Dp,
    bottomLeft: Dp,
    bottomRight: Dp,
    isMyself: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = if (isMyself) Alignment.End else Alignment.Start,
        modifier = modifier.fillMaxWidth().width(200.dp),
    ) {
        Text(
            text = username,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 0.dp, top = 8.dp),
        )
        Column {
            val primaryColor = MaterialTheme.colorScheme.primary
            val secondaryColor = MaterialTheme.colorScheme.secondary
            Text(
                text = message,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier =
                    Modifier.drawBehind {
                            // Draw a background for the message bubble
                            drawCustomBubble(
                                color =
                                    if (isMyself) {
                                        primaryColor
                                    } else {
                                        secondaryColor
                                    },
                                size = size,
                                topLeft = topLeft.toPx(), // squared
                                topRight = topRight.toPx(),
                                bottomRight = bottomRight.toPx(),
                                bottomLeft = bottomLeft.toPx(),
                            )
                        }
                        .padding(16.dp),
            )
            MessageReceipt(timestamp = timestamp, modifier = Modifier.align(Alignment.End))
        }
    }
}

fun DrawScope.drawCustomBubble(
    color: Color,
    size: Size,
    topLeft: Float,
    topRight: Float,
    bottomRight: Float,
    bottomLeft: Float,
) {
    val path =
        Path().apply {
            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    topLeftCornerRadius = CornerRadius(topLeft, topLeft),
                    topRightCornerRadius = CornerRadius(topRight, topRight),
                    bottomRightCornerRadius = CornerRadius(bottomRight, bottomRight),
                    bottomLeftCornerRadius = CornerRadius(bottomLeft, bottomLeft),
                )
            )
        }
    drawPath(path, color)
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MessageBubblePreview() {
    GymAppTheme {
        Scaffold { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).padding(8.dp)) {
                MessageBubble(
                    message = "Hello",
                    isMyself = true,
                    timestamp = "2023-10-01 12:34:56",
                    username = "User1",
                )

                Spacer(Modifier.padding(10.dp))

                MessageBubble(
                    message = "Hello, this is a test message!",
                    isMyself = false,
                    timestamp = "2023-10-01 12:35:56",
                    username = "User2",
                )
            }
        }
    }
}
