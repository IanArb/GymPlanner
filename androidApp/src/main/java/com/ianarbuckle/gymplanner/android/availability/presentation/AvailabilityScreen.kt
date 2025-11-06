package com.ianarbuckle.gymplanner.android.availability.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.availability.AvailabilityUiState
import com.ianarbuckle.gymplanner.android.availability.AvailabilityViewModel
import com.ianarbuckle.gymplanner.android.availability.presentation.state.AvailabilityContentState
import com.ianarbuckle.gymplanner.android.availability.presentation.state.AvailabilityData
import com.ianarbuckle.gymplanner.android.availability.presentation.state.AvailabilityScreenState
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import com.ianarbuckle.gymplanner.android.utils.currentWeekDates
import com.ianarbuckle.gymplanner.android.utils.isCurrentDay
import com.ianarbuckle.gymplanner.android.utils.toLocalTime
import com.ianarbuckle.gymplanner.availability.domain.Time
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailabilityScreen(
    paddingValues: PaddingValues,
    availabilityScreenState: AvailabilityScreenState,
    onBookingClick: (AvailabilityData) -> Unit,
    modifier: Modifier = Modifier,
    availabilityViewModel: AvailabilityViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) { availabilityViewModel.fetchAvailability() }

    val bookingState = availabilityViewModel.availabilityUiState.collectAsState()

    val daysOfWeek: ImmutableList<String> = currentWeekDates().toImmutableList()

    val today = daysOfWeek.find { it.isCurrentDay() } ?: ""

    val selectedTimeSlotId = remember { mutableStateOf("") }
    val selectedTimeSlot = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf(today) }
    val availableTimes = remember { mutableStateOf(emptyList<Time>()) }

    when (bookingState.value) {
        is AvailabilityUiState.Idle -> {
            // noop
        }

        is AvailabilityUiState.Loading -> {
            AvailabilityLoadingShimmer(
                modifier = modifier,
                paddingValues = paddingValues,
                shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View),
            )
        }

        is AvailabilityUiState.Failed -> {
            RetryErrorScreen(
                modifier = modifier,
                text = "Failed to load availability.",
                onClick = { availabilityViewModel.fetchAvailability() },
            )
        }

        is AvailabilityUiState.AvailabilitySuccess -> {
            val success = bookingState.value as AvailabilityUiState.AvailabilitySuccess
            val availabilitySlots = success.availability.slots
            val isAvailable = success.isPersonalTrainerAvailable

            fun getAvailableTimesForSelectedDate(selectedDate: String): List<Time> {
                return availabilitySlots.find { it.date.contains(selectedDate) }?.times
                    ?: emptyList()
            }

            availableTimes.value = getAvailableTimesForSelectedDate(selectedDate.value)

            val calendarPagerState = rememberPagerState {
                (daysOfWeek.size + CalendarPagerSize) / PagerOffset
            }

            val rowsPerPage = TimeslotPickerPageSize
            val itemsPerPage = rowsPerPage * TimeslotPickerPageSize

            val timeSlotPagerState = rememberPagerState {
                availableTimes.value.chunked(itemsPerPage).size
            }

            val verticalScroll = rememberScrollState()

            val contentState =
                AvailabilityContentState(
                    personalTrainerLabel = "Personal Trainer",
                    name = availabilityScreenState.personalTrainer.name,
                    imageUrl = availabilityScreenState.personalTrainer.imageUrl,
                    qualifications = availabilityScreenState.personalTrainer.qualifications,
                    daysOfWeek = daysOfWeek,
                    availableTimes = availableTimes.value.toImmutableList(),
                    selectedDate = selectedDate.value,
                    selectedTimeSlot = selectedTimeSlotId.value,
                    isAvailable = isAvailable,
                    timeslotRowsPerPage = rowsPerPage,
                    timeslotItemsPerPage = itemsPerPage,
                )

            AvailabilityContent(
                paddingValues = paddingValues,
                contentState = contentState,
                calendarPagerState = calendarPagerState,
                timeSlotPagerState = timeSlotPagerState,
                verticalScrollState = verticalScroll,
                modifier = modifier,
                onSelectedDateChange = { selectedDate.value = it },
                onTimeSlotChange = { id, time ->
                    selectedTimeSlotId.value = id
                    selectedTimeSlot.value = time
                },
                onBookClick = {
                    onBookingClick(
                        AvailabilityData(
                            timeSlotId = selectedTimeSlotId.value,
                            selectedDate = selectedDate.value,
                            selectedTimeSlot = selectedTimeSlot.value.toLocalTime(),
                        )
                    )
                },
            )
        }
    }
}

private const val CalendarPagerSize = 4
private const val PagerOffset = 5
private const val TimeslotPickerPageSize = 3
