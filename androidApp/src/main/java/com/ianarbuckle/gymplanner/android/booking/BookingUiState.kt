package com.ianarbuckle.gymplanner.android.booking

import com.ianarbuckle.gymplanner.booking.domain.BookingResponse

sealed interface BookingUiState {

  data class Success(val booking: BookingResponse) : BookingUiState

  object Failed : BookingUiState

  object Loading : BookingUiState

  object Idle : BookingUiState
}
