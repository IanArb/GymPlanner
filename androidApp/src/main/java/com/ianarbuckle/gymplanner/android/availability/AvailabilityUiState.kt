package com.ianarbuckle.gymplanner.android.availability

import com.ianarbuckle.gymplanner.availability.domain.Availability

sealed interface AvailabilityUiState {
  data object Idle : AvailabilityUiState

  data object Failed : AvailabilityUiState

  data object Loading : AvailabilityUiState

  data class AvailabilitySuccess(
    val availability: Availability,
    val isPersonalTrainerAvailable: Boolean,
  ) : AvailabilityUiState
}
