package com.ianarbuckle.gymplanner.android.workout

import com.ianarbuckle.gymplanner.model.Client

sealed interface ClientWorkoutUiState {

    class ClientClientWorkout(
        val clients: List<Client>
    ) : ClientWorkoutUiState

    data object Failure : ClientWorkoutUiState

    data object Loading : ClientWorkoutUiState

    data object Idle : ClientWorkoutUiState

    data object ClientWorkoutExpired : ClientWorkoutUiState
}