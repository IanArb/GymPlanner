package com.ianarbuckle.gymplanner.android.gymlocations.data

import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import kotlinx.collections.immutable.ImmutableList

sealed interface GymLocationsUiState {

    data class Success(val gymLocations: ImmutableList<GymLocations>) : GymLocationsUiState

    data object Failure : GymLocationsUiState

    data object Loading : GymLocationsUiState
}
