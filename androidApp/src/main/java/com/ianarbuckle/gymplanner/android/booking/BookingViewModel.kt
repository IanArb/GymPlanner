package com.ianarbuckle.gymplanner.android.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.booking.domain.Booking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val gymPlanner: GymPlanner,
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider,
): ViewModel() {

    private val _bookingState = MutableStateFlow<BookingUiState>(BookingUiState.Loading)
    val bookingState = _bookingState.asStateFlow()

    fun saveBooking(booking: Booking) {
        viewModelScope.launch(coroutinesDispatcherProvider.io) {
            gymPlanner.saveBooking(booking).fold(
                onSuccess = { response ->
                    _bookingState.value = BookingUiState.BookingSuccess(response)
                },
                onFailure = {
                    _bookingState.value = BookingUiState.Failed
                }
            )
        }

    }

}