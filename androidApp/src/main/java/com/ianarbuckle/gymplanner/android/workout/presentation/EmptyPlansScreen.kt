package com.ianarbuckle.gymplanner.android.workout.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun EmptyPlansWorkout(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("You have no existing plans.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EmptyPlansPreview() {
    GymAppTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Workouts") }) },
        ) { padding ->
            EmptyPlansWorkout(
                modifier = Modifier.padding(paddingValues = padding)
            )
        }
    }
}