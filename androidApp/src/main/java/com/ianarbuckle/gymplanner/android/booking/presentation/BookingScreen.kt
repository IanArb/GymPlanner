package com.ianarbuckle.gymplanner.android.booking.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.booking.BookingUiState
import com.ianarbuckle.gymplanner.android.booking.BookingViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import com.ianarbuckle.gymplanner.android.utils.currentWeekDates
import com.ianarbuckle.gymplanner.availability.domain.Time

@Composable
fun BookingScreen(
    paddingValues: PaddingValues,
    bookingViewModel: BookingViewModel = hiltViewModel(),
    name: String,
    imageUrl: String,
    qualifications: List<String>,
) {
    val bookingState = bookingViewModel.bookingState.collectAsState()

    val daysOfWeek: List<String> = currentWeekDates()

    val selectedTimeSlot = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf("") }
    val availableTimes = remember { mutableStateOf(emptyList<Time>()) }

    when (bookingState.value) {
        is BookingUiState.Idle -> {
            //noop
        }

        is BookingUiState.Loading -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is BookingUiState.BookingSuccess -> {
            // Show success state
        }
        is BookingUiState.Failed -> {
            RetryErrorScreen(
                text = "Failed to load availability",
            ) {
                //TODO retry
            }
        }

        is BookingUiState.BookingsSuccess -> {

        }

        is BookingUiState.AvailabilitySuccess -> {
            val success = bookingState.value as BookingUiState.AvailabilitySuccess
            val availabilitySlots = success.availability.slots
            val isAvailable = success.isPersonalTrainerAvailable

            fun getAvailableTimesForSelectedDate(selectedDate: String): List<Time> {
                return availabilitySlots.find { it.date.contains(selectedDate) }?.times ?: emptyList()
            }

            LaunchedEffect(selectedDate.value) {
                availableTimes.value = getAvailableTimesForSelectedDate(selectedDate.value)
            }

            BookingContent(
                paddingValues = paddingValues,
                personalTrainerLabel = "Personal Trainer",
                name = name,
                imageUrl = imageUrl,
                qualifications = qualifications,
                daysOfWeek = daysOfWeek,
                availableTimes = availableTimes.value,
                selectedDate = selectedDate.value,
                selectedTimeSlot = selectedTimeSlot.value,
                isAvailable = isAvailable,
                onSelectedDateChange = {
                    selectedDate.value = it
                },
                onTimeSlotChange = {
                    selectedTimeSlot.value = it
                },
                onBookClick = {

                }
            )
        }
    }
}