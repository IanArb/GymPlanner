package com.ianarbuckle.gymplanner.android.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun RetryErrorScreen(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text)
        Row(
            modifier = Modifier.clickable {
                onClick()
            },
        ) {
            Text("Tap to retry")
            Spacer(modifier = Modifier.padding(6.dp))
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Retry icon")
        }
    }
}

@Preview
@Composable
private fun EmptyWorkoutPreview() {
    GymAppTheme {
        Column(
            modifier = Modifier.background(Color.White),
        ) {
            RetryErrorScreen(
                text = "Failed to retrieve you gym plan",
                modifier = Modifier.padding(paddingValues = PaddingValues(16.dp)),
                onClick = {

                }
            )
        }
    }
}
