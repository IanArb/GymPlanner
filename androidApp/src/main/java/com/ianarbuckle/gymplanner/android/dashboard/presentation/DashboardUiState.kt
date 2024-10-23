package com.ianarbuckle.gymplanner.android.dashboard.presentation

import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import kotlinx.collections.immutable.ImmutableList

sealed interface DashboardUiState {

    data class FitnessClasses(val items: ImmutableList<FitnessClass>): DashboardUiState

    data object Failure : DashboardUiState

    data object Loading : DashboardUiState
}