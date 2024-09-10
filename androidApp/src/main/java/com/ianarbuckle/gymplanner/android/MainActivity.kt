package com.ianarbuckle.gymplanner.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.workout.ClientWorkoutUiState
import com.ianarbuckle.gymplanner.android.workout.WorkoutViewModel
import com.ianarbuckle.gymplanner.android.workout.ui.EmptyPlansWorkout
import com.ianarbuckle.gymplanner.android.workout.ui.WorkoutContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WorkoutViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Gym Plan") }) },
                ) { contentPadding ->
                    val state = viewModel.uiState.collectAsState()

                    when (val uiState = state.value) {
                        ClientWorkoutUiState.Failure -> {

                        }
                        ClientWorkoutUiState.Idle -> {

                        }
                        ClientWorkoutUiState.Loading -> {
                            
                        }
                        is ClientWorkoutUiState.ClientClientWorkout -> {
                            if (uiState.clients.isNotEmpty()) {
                                WorkoutContent(
                                    contentPadding,
                                    uiState.clients.first(),
                                ) {

                                }
                            } else {
                                EmptyPlansWorkout()
                            }
                        }

                        ClientWorkoutUiState.ClientWorkoutExpired -> TODO()
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Gym Plan") }) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    // TODO
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                }
            }
        ) {
            WorkoutContent(
                it,
                DataProvider.client()
            ) {

            }
        }
    }
}
