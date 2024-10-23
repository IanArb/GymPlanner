package com.ianarbuckle.gymplanner.android.personaltrainers.data

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer


sealed interface PersonalTrainersState {

    data class Success(val personalTrainers: List<PersonalTrainer>) : PersonalTrainersState

    data object Failure : PersonalTrainersState

    data object Loading : PersonalTrainersState
}