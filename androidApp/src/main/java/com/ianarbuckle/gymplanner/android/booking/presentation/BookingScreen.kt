package com.ianarbuckle.gymplanner.android.booking.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.booking.BookingUiState
import com.ianarbuckle.gymplanner.android.booking.BookingViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import com.ianarbuckle.gymplanner.android.utils.currentWeekDates
import com.ianarbuckle.gymplanner.availability.domain.Time

@Composable
fun BookingScreen(
    paddingValues: PaddingValues,
    name: String,
    imageUrl: String,
    qualifications: List<String>,
    modifier: Modifier = Modifier,
    bookingViewModel: BookingViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        bookingViewModel.fetchAvailability()
    }

    val bookingState = bookingViewModel.bookingState.collectAsState()

    val daysOfWeek: List<String> = currentWeekDates()

    val selectedTimeSlot = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf("") }
    val availableTimes = remember { mutableStateOf(emptyList<Time>()) }

    when (bookingState.value) {
        is BookingUiState.Idle -> {
            // noop
        }

        is BookingUiState.Loading -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is BookingUiState.BookingSuccess -> {
            // Show success state
        }
        is BookingUiState.Failed -> {
            RetryErrorScreen(
                modifier = modifier,
                text = "Failed to load availability.",
                onClick = {
                    bookingViewModel.fetchAvailability()
                },
            )
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

            val calendarPagerState = rememberPagerState {
                (daysOfWeek.size + CalendarPagerSize) / PagerOffset
            }

            val rowsPerPage = TimeslotPickerPageSize
            val itemsPerPage = rowsPerPage * TimeslotPickerPageSize

            val timeSlotPagerState =
                rememberPagerState { availableTimes.value.chunked(itemsPerPage).size }

            val verticalScroll = rememberScrollState()

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
                calendarPagerState = calendarPagerState,
                timeSlotPagerState = timeSlotPagerState,
                verticalScrollState = verticalScroll,
                timeslotRowsPerPage = rowsPerPage,
                timeslotItemsPerPage = itemsPerPage,
                modifier = modifier,
                onSelectedDateChange = {
                    selectedDate.value = it
                },
                onTimeSlotChange = {
                    selectedTimeSlot.value = it
                },
                onBookClick = {
                },
            )
        }
    }
}

private const val CalendarPagerSize = 4
private const val PagerOffset = 5
private const val TimeslotPickerPageSize = 3
