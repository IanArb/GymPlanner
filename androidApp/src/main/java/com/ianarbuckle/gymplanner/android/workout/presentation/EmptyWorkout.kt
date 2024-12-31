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
fun EmptyWorkout(
    personalTrainerName: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("You have no gym program.")
        Text("Contact $personalTrainerName to add you a plan.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun EmptyWorkoutPreview() {
    GymAppTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Workouts") }) },
        ) { padding ->
            EmptyWorkout(
                personalTrainerName = "Ian",
                modifier = Modifier.padding(paddingValues = padding),
            )
        }
    }
}
