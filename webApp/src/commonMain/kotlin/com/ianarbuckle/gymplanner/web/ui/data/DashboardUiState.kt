package com.ianarbuckle.gymplanner.web.ui.data

import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import com.ianarbuckle.gymplanner.web.ui.trainers.TrainerItem
import kotlinx.collections.immutable.ImmutableList

sealed class DashboardUiState {
    object Idle : DashboardUiState()

    object Loading : DashboardUiState()

    data class Success(val facilities: ImmutableList<FacilityStatus>) : DashboardUiState()

    data class Error(val message: String) : DashboardUiState()
}

sealed class TrainersUiState {
    object Idle : TrainersUiState()

    object Loading : TrainersUiState()

    data class Success(val trainers: ImmutableList<TrainerItem>) : TrainersUiState()

    data class Error(val message: String) : TrainersUiState()
}
