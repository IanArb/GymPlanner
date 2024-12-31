package com.ianarbuckle.gymplanner.android.booking

import androidx.compose.runtime.Stable
import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import kotlinx.collections.immutable.ImmutableList

sealed interface BookingUiState {
    data object Idle : BookingUiState

    data class BookingSuccess(@Stable val booking: BookingResponse) : BookingUiState

    data object Failed : BookingUiState

    data class BookingsSuccess(val bookings: ImmutableList<Booking>) : BookingUiState

    data object Loading : BookingUiState

    data class AvailabilitySuccess(
        val availability: Availability,
        val isPersonalTrainerAvailable: Boolean,
    ) : BookingUiState
}
