package com.ianarbuckle.gymplanner.android.personaltrainers.data

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import kotlinx.collections.immutable.ImmutableList


sealed interface PersonalTrainersState {

    data class Success(val personalTrainers: ImmutableList<PersonalTrainer>) : PersonalTrainersState

    data object Failure : PersonalTrainersState

    data object Loading : PersonalTrainersState
}