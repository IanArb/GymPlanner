package com.ianarbuckle.gymplanner.android.booking.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ianarbuckle.gymplanner.android.booking.BookingUiState
import com.ianarbuckle.gymplanner.android.booking.BookingViewModel
import com.ianarbuckle.gymplanner.android.utils.displayTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailsScreen(
    contentPadding: PaddingValues,
    bookingDetailsData: BookingDetailsData,
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    bookingViewModel: BookingViewModel = hiltViewModel(),
) {
    val uiState by bookingViewModel.bookingUiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.padding(contentPadding)) {
        when (uiState) {
            is BookingUiState.Idle -> {
                BookingDetailsContent(
                    selectedDate = bookingDetailsData.selectedDate,
                    selectedTimeSlot = bookingDetailsData.selectedTimeSlot,
                    personalTrainerName = bookingDetailsData.personalTrainerName,
                    personalTrainerAvatarUrl = bookingDetailsData.personalTrainerAvatarUrl,
                    location = bookingDetailsData.location,
                    onConfirmClick = { saveBooking(bookingViewModel, bookingDetailsData) },
                )
            }
            is BookingUiState.Failed -> {
                BookingFailedContent(
                    onRetry = { saveBooking(bookingViewModel, bookingDetailsData) }
                )
            }
            is BookingUiState.Loading -> {
                BookingDetailsContent(
                    selectedDate = bookingDetailsData.selectedDate,
                    selectedTimeSlot = bookingDetailsData.selectedTimeSlot,
                    personalTrainerName = bookingDetailsData.personalTrainerName,
                    personalTrainerAvatarUrl = bookingDetailsData.personalTrainerAvatarUrl,
                    location = bookingDetailsData.location,
                    onConfirmClick = { saveBooking(bookingViewModel, bookingDetailsData) },
                    isLoading = true,
                )
            }
            is BookingUiState.Success -> {
                BookingConfirmationContent(
                    trainerName = bookingDetailsData.personalTrainerName,
                    avatarUrl = bookingDetailsData.personalTrainerAvatarUrl,
                    sessionDate = bookingDetailsData.selectedDate,
                    sessionTime = bookingDetailsData.selectedTimeSlot.displayTime(),
                    location = bookingDetailsData.location,
                    goHomeClick = { navigateToHomeScreen() },
                )
            }
        }
    }
}

private fun saveBooking(
    bookingViewModel: BookingViewModel,
    bookingDetailsData: BookingDetailsData,
) {
    bookingViewModel.saveBooking(bookingDetailsData = bookingDetailsData)
}
