package com.ianarbuckle.gymplanner.android.workout.data

import com.ianarbuckle.gymplanner.clients.domain.Client
import kotlinx.collections.immutable.ImmutableList

sealed interface ClientWorkoutUiState {

    class ClientClientWorkout(
        val clients: ImmutableList<Client>,
    ) : ClientWorkoutUiState

    data object Failure : ClientWorkoutUiState

    data object Loading : ClientWorkoutUiState

    data object ClientWorkoutExpired : ClientWorkoutUiState
}
