package com.ianarbuckle.gymplanner.android.booking.presentation.bookingscreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.booking.BookingUiState
import com.ianarbuckle.gymplanner.android.booking.BookingViewModel
import com.ianarbuckle.gymplanner.android.booking.presentation.bookingdetails.BookingDetailsScreen
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.currentWeekDates
import com.ianarbuckle.gymplanner.android.utils.displayTime
import com.ianarbuckle.gymplanner.android.utils.isCurrentDay
import com.ianarbuckle.gymplanner.android.utils.toLocalTime
import com.ianarbuckle.gymplanner.availability.domain.Time
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    paddingValues: PaddingValues,
    name: String,
    imageUrl: String,
    qualifications: List<String>,
    gymLocation: String,
    modifier: Modifier = Modifier,
    bookingViewModel: BookingViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        bookingViewModel.fetchAvailability()
    }

    val bookingState = bookingViewModel.bookingState.collectAsState()

    val daysOfWeek: List<String> = currentWeekDates()

    val today = daysOfWeek.find { it.isCurrentDay() } ?: ""

    val selectedTimeSlotId = remember { mutableStateOf("") }
    val selectedTimeSlot = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf(today) }
    val availableTimes = remember { mutableStateOf(emptyList<Time>()) }

    when (bookingState.value) {
        is BookingUiState.Idle -> {
            // noop
        }

        is BookingUiState.Loading -> {
            BookingLoadingShimmer(
                modifier = modifier,
                paddingValues = paddingValues,
                shimmer = rememberShimmer(
                    shimmerBounds = ShimmerBounds.View,
                ),
            )
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

            availableTimes.value = getAvailableTimesForSelectedDate(selectedDate.value)

            val calendarPagerState = rememberPagerState {
                (daysOfWeek.size + CalendarPagerSize) / PagerOffset
            }

            val rowsPerPage = TimeslotPickerPageSize
            val itemsPerPage = rowsPerPage * TimeslotPickerPageSize

            val timeSlotPagerState =
                rememberPagerState { availableTimes.value.chunked(itemsPerPage).size }

            val verticalScroll = rememberScrollState()
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )
            val coroutineScope = rememberCoroutineScope()

            BookingContent(
                paddingValues = paddingValues,
                personalTrainerLabel = "Personal Trainer",
                name = name,
                imageUrl = imageUrl,
                qualifications = qualifications,
                daysOfWeek = daysOfWeek,
                availableTimes = availableTimes.value,
                selectedDate = selectedDate.value,
                selectedTimeSlot = selectedTimeSlotId.value,
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
                onTimeSlotChange = { id, time ->
                    selectedTimeSlotId.value = id
                    selectedTimeSlot.value = time
                },
                onBookClick = {
                    coroutineScope.launch {
                        if (!sheetState.isVisible) {
                            sheetState.show()
                        }
                    }
                },
            )

            if (sheetState.isVisible) {
                BookingDetailsScreen(
                    selectedDate = selectedDate.value,
                    selectedTimeSlot = selectedTimeSlot.value.toLocalTime().displayTime(),
                    location = gymLocation,
                    personalTrainerName = name,
                    personalTrainerAvatarUrl = imageUrl,
                    sheetState = sheetState,
                    onDismissClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                    },
                    onConfirmClick = {
                    },
                )
            }
        }
    }
}

private const val CalendarPagerSize = 4
private const val PagerOffset = 5
private const val TimeslotPickerPageSize = 3

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BookingScreenPreview() {
    GymAppTheme {
        Scaffold(
            topBar = {
                // TopAppBar
                TopAppBar(
                    title = {
                        Text("Booking")
                    },
                    navigationIcon = { },
                    actions = { },
                )
            },
        ) { padding ->
            BookingScreen(
                paddingValues = padding,
                name = "John Doe",
                imageUrl = "https://example.com/avatar.jpg",
                qualifications = listOf("Qualification 1", "Qualification 2"),
                gymLocation = "Clontarf",
            )
        }
    }
}
