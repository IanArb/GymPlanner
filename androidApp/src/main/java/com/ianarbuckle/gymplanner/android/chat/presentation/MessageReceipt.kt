package com.ianarbuckle.gymplanner.android.chat.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined
import com.ianarbuckle.gymplanner.android.utils.toDisplayTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun MessageReceipt(timestamp: String, hasFailedMessage: Boolean, modifier: Modifier = Modifier) {
    if (hasFailedMessage) {
        Text(
            text = "Tap to retry",
            modifier = modifier,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
        )
    } else {
        Text(text = timestamp.toDisplayTime(), style = MaterialTheme.typography.bodySmall)
    }
}

@OptIn(ExperimentalTime::class)
@PreviewsCombined
@Composable
private fun MessageReceiptPreview() {
    GymAppTheme {
        Column {
            MessageReceipt(
                timestamp = Clock.System.now().toString(),
                modifier = Modifier,
                hasFailedMessage = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            MessageReceipt(
                timestamp = Clock.System.now().toString(),
                modifier = Modifier,
                hasFailedMessage = false,
            )
        }
    }
}
