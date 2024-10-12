package com.ianarbuckle.gymplanner.android.core.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RetryErrorScreen(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    ) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text)
        Row(modifier = Modifier.clickable {
            onClick()
        }) {
            Text("Tap to retry")
            Spacer(modifier = Modifier.padding(6.dp))
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EmptyWorkoutPreview() {
    GymPlannerTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Workouts") }) },
        ) { padding ->
            RetryErrorScreen(
                text = "Failed to retrieve you gym plan",
                modifier = Modifier.padding(paddingValues = padding)
            ) {

            }
        }
    }
}