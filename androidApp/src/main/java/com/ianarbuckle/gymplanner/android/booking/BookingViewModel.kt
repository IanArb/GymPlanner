package com.ianarbuckle.gymplanner.android.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.android.booking.presentation.BookingDetailsData
import com.ianarbuckle.gymplanner.android.utils.toGymLocation
import com.ianarbuckle.gymplanner.booking.BookingRepository
import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    private val _bookingUiState = MutableStateFlow<BookingUiState>(BookingUiState.Idle)
    val bookingUiState = _bookingUiState.asStateFlow()

    fun saveBooking(bookingDetailsData: BookingDetailsData) {
        _bookingUiState.update {
            BookingUiState.Loading
        }

        viewModelScope.launch {
            val userId = dataStoreRepository.getStringData(USER_ID) ?: ""

            val booking = Booking(
                timeSlotId = bookingDetailsData.timeSlotId,
                bookingDate = bookingDetailsData.selectedDate,
                startTime = bookingDetailsData.selectedTimeSlot,
                personalTrainer = PersonalTrainer(
                    id = bookingDetailsData.personalTrainerId,
                    name = bookingDetailsData.personalTrainerName,
                    imageUrl = bookingDetailsData.personalTrainerAvatarUrl,
                    gymLocation = bookingDetailsData.location.toGymLocation(),
                ),
                userId = userId,
            )

            bookingRepository.saveBooking(booking).fold(
                onSuccess = { response ->
                    _bookingUiState.update {
                        BookingUiState.Success(response)
                    }
                },
                onFailure = {
                    _bookingUiState.update {
                        BookingUiState.Failed
                    }
                },
            )
        }
    }
}
