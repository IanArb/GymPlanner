package com.ianarbuckle.gymplanner.android.dashboard.data

import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.profile.domain.Profile
import kotlinx.collections.immutable.ImmutableList

sealed interface DashboardUiState {

    data class Success(
        val items: ImmutableList<FitnessClass>,
        val profile: Profile,
        val booking: ImmutableList<BookingResponse>,
    ) : DashboardUiState

    data object Failure : DashboardUiState

    data object Loading : DashboardUiState

    data object Idle : DashboardUiState
}
