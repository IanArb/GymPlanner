package com.ianarbuckle.gymplanner.android.chat.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.toDisplayTime

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

@Preview(showBackground = true)
@Composable
private fun MessageReceiptPreview() {
    GymAppTheme {
        Column {
            MessageReceipt(
                timestamp = "2023-10-01 12:34:56",
                modifier = Modifier,
                hasFailedMessage = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            MessageReceipt(
                timestamp = "2023-10-01 12:34:56",
                modifier = Modifier,
                hasFailedMessage = false,
            )
        }
    }
}
