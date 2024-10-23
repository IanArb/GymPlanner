package com.ianarbuckle.gymplanner.android.gymlocations.data

import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations


sealed interface GymLocationsUiState {

    data class Success(val gymLocations: List<GymLocations>) : GymLocationsUiState

    data object Failure : GymLocationsUiState

    data object Loading : GymLocationsUiState
}