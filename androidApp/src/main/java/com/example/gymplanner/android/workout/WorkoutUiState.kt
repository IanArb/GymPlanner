package com.example.gymplanner.android.workout

sealed class WorkoutUiState {

    class Success(private val id: String) : WorkoutUiState()

    data object Failure : WorkoutUiState()

    data object Loading : WorkoutUiState()
}