package com.ianarbuckle.gymplanner.android.dashboard.data

import androidx.compose.runtime.Stable
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.profile.domain.Profile
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed interface DashboardUiState {

    data class Success(val items: ImmutableList<FitnessClass>, @Stable val profile: Profile): DashboardUiState

    data object Failure : DashboardUiState

    data object Loading : DashboardUiState

    data object Idle : DashboardUiState
}