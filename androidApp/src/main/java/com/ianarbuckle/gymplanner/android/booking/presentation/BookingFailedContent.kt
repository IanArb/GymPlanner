package com.ianarbuckle.gymplanner.android.booking.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BookingFailedContent(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Booking Failed",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(96.dp),
        )

        Text(
            text = "Booking Failed",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 16.dp),
        )

        Text(
            text =
                "We couldn’t confirm your session. Please check your internet connection and try again.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp).fillMaxWidth(TextMaxWidth),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onRetry, modifier = Modifier.weight(1f)) { Text("Retry") }
        }
    }
}

private const val TextMaxWidth = 0.85f

@Preview(showBackground = true)
@Composable
private fun BookingFailedContentPreview() {
    BookingFailedContent(onRetry = {})
}
