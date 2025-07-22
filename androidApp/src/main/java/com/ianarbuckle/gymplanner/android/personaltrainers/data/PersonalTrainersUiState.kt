package com.ianarbuckle.gymplanner.android.personaltrainers.data

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import kotlinx.collections.immutable.ImmutableList

sealed interface PersonalTrainersUiState {

    data class Success(val personalTrainers: ImmutableList<PersonalTrainer>) :
        PersonalTrainersUiState

    data object Failure : PersonalTrainersUiState

    data object Loading : PersonalTrainersUiState
}
