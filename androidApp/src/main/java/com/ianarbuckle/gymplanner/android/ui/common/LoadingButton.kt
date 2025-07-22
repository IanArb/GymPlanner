package com.ianarbuckle.gymplanner.android.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(onClick = onClick, modifier = modifier, enabled = !isLoading) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp).padding(end = 8.dp),
                strokeWidth = 2.dp,
            )
        }
        Text(text = text)
    }
}

@Preview
@Composable
private fun LoadingButtonPreview(modifier: Modifier = Modifier) {
    LoadingButton(text = "Click me", isLoading = false, modifier = modifier, onClick = {})
}
