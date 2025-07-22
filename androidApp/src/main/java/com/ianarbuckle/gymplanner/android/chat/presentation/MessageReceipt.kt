package com.ianarbuckle.gymplanner.android.chat.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.toDisplayTime

@Composable
fun MessageReceipt(timestamp: String, modifier: Modifier = Modifier) {
  Text(
    text = timestamp.toDisplayTime(),
    modifier = modifier,
    style = MaterialTheme.typography.bodySmall,
  )
}

@Preview
@Composable
private fun MessageReceiptPreview() {
  GymAppTheme { Surface { MessageReceipt(timestamp = "2023-10-01 12:34:56", modifier = Modifier) } }
}
