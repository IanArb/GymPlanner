package com.ianarbuckle.gymplanner.android.booking.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.booking.BookingUiState
import com.ianarbuckle.gymplanner.android.booking.BookingViewModel
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.displayTime
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailsScreen(
    contentPadding: PaddingValues,
    bookingDetailsData: BookingDetailsData,
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    bookingViewModel: BookingViewModel = hiltViewModel(),
) {
    val uiState = bookingViewModel.bookingUiState.collectAsState()

    Column(
        modifier = modifier.padding(contentPadding),
    ) {
        when (uiState.value) {
            is BookingUiState.Idle -> {
                BookingDetailsContent(
                    selectedDate = bookingDetailsData.selectedDate,
                    selectedTimeSlot = bookingDetailsData.selectedTimeSlot,
                    personalTrainerName = bookingDetailsData.personalTrainerName,
                    personalTrainerAvatarUrl = bookingDetailsData.personalTrainerAvatarUrl,
                    location = bookingDetailsData.location,
                    onConfirmClick = {
                        saveBooking(bookingViewModel, bookingDetailsData)
                    },
                )
            }
            is BookingUiState.Failed -> {
                BookingFailedContent(
                    onRetry = {
                        saveBooking(bookingViewModel, bookingDetailsData)
                    },
                )
            }
            is BookingUiState.Loading -> {
                BookingDetailsContent(
                    selectedDate = bookingDetailsData.selectedDate,
                    selectedTimeSlot = bookingDetailsData.selectedTimeSlot,
                    personalTrainerName = bookingDetailsData.personalTrainerName,
                    personalTrainerAvatarUrl = bookingDetailsData.personalTrainerAvatarUrl,
                    location = bookingDetailsData.location,
                    onConfirmClick = {
                        saveBooking(bookingViewModel, bookingDetailsData)
                    },
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
                    goHomeClick = {
                        navigateToHomeScreen()
                    },
                )
            }
        }
    }
}

private fun saveBooking(
    bookingViewModel: BookingViewModel,
    bookingDetailsData: BookingDetailsData,
) {
    bookingViewModel.saveBooking(
        bookingDetailsData = bookingDetailsData,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BookingConfirmationScreenPreview() {
    GymAppTheme {
        Column {
            BookingDetailsScreen(
                contentPadding = PaddingValues(16.dp),
                bookingDetailsData = BookingDetailsData(
                    timeSlotId = "1",
                    personalTrainerId = "1",
                    selectedDate = "2023-01-01",
                    selectedTimeSlot = LocalTime.parse("10:00:00"),
                    personalTrainerName = "John Doe",
                    personalTrainerAvatarUrl = "https://example.com/avatar.jpg",
                    location = "Gym Location",
                ),
                navigateToHomeScreen = {
                },
            )
        }
    }
}
