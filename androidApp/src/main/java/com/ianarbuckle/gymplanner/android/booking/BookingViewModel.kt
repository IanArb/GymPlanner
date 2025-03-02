package com.ianarbuckle.gymplanner.android.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.android.utils.calendarMonth
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import com.ianarbuckle.gymplanner.booking.BookingRepository
import com.ianarbuckle.gymplanner.booking.domain.Booking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val availabilityRepository: AvailabilityRepository,
    private val savedStateHandle: SavedStateHandle,
    private val clock: Clock,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel() {

    private val _bookingState = MutableStateFlow<BookingUiState>(BookingUiState.Idle)
    val bookingState = _bookingState.asStateFlow()

    fun fetchAvailability() {
        viewModelScope.launch(dispatcherProvider.io) {
            val personalTrainerId = savedStateHandle.get<String>("personalTrainerId")
            val currentDateTime = clock.now().toLocalDateTime(TimeZone.currentSystemDefault())

            _bookingState.update {
                BookingUiState.Loading
            }

            if (!personalTrainerId.isNullOrEmpty()) {
                val checkAvailabilityDeferred = async {
                    availabilityRepository.checkAvailability(
                        personalTrainerId = personalTrainerId,
                        month = currentDateTime.calendarMonth(),
                    )
                }

                val fetchAvailabilityDeferred = async {
                    availabilityRepository.getAvailability(
                        personalTrainerId = personalTrainerId,
                        month = currentDateTime.calendarMonth(),
                    )
                }

                supervisorScope {
                    val checkAvailabilityResponse = checkAvailabilityDeferred.await()
                    val fetchAvailabilityResponse = fetchAvailabilityDeferred.await()

                    launch {
                        val state = fetchAvailabilityResponse.fold(
                            onSuccess = { availability ->
                                checkAvailabilityResponse.fold(
                                    onSuccess = { checkAvailability ->
                                        BookingUiState.AvailabilitySuccess(
                                            availability = availability,
                                            isPersonalTrainerAvailable = checkAvailability.isAvailable,
                                        )
                                    },
                                    onFailure = {
                                        BookingUiState.Failed
                                    },
                                )
                            },
                            onFailure = {
                                BookingUiState.Failed
                            },
                        )

                        _bookingState.update {
                            state
                        }
                    }
                }
            } else {
                _bookingState.update {
                    BookingUiState.Failed
                }
            }
        }
    }

    fun saveBooking(booking: Booking) {
        _bookingState.update {
            BookingUiState.Loading
        }
        viewModelScope.launch(dispatcherProvider.io) {
            bookingRepository.saveBooking(booking).fold(
                onSuccess = { response ->
                    _bookingState.value = BookingUiState.BookingSuccess(response)
                },
                onFailure = {
                    _bookingState.value = BookingUiState.Failed
                },
            )
        }
    }
}
