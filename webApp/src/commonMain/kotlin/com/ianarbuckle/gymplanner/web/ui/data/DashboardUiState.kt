package com.ianarbuckle.gymplanner.web.ui.data

import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import kotlinx.collections.immutable.ImmutableList

sealed class DashboardUiState {
    object Idle : DashboardUiState()

    object Loading : DashboardUiState()

    data class Success(val facilities: ImmutableList<FacilityStatus>) : DashboardUiState()

    data class Error(val message: String) : DashboardUiState()
}
