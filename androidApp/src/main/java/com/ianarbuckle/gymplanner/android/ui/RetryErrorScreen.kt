package com.ianarbuckle.gymplanner.android.ui

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
import com.ianarbuckle.gymplanner.android.MyApplicationTheme

@Composable
fun RetryErrorScreen(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Failed to retrieve your gym plan.")
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
    MyApplicationTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Workouts") }) },
        ) { padding ->
            RetryErrorScreen(
                modifier = Modifier.padding(paddingValues = padding)
            ) {

            }
        }
    }
}