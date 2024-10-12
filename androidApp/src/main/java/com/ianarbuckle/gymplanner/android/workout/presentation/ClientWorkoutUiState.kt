package com.ianarbuckle.gymplanner.android.workout.presentation

import com.ianarbuckle.gymplanner.model.Client

sealed interface ClientWorkoutUiState {

    class ClientClientWorkout(
        val clients: List<Client>
    ) : ClientWorkoutUiState

    data object Failure : ClientWorkoutUiState

    data object Loading : ClientWorkoutUiState

    data object ClientWorkoutExpired : ClientWorkoutUiState
}