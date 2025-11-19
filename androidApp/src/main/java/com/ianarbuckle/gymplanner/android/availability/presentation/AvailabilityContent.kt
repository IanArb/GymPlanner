package com.ianarbuckle.gymplanner.android.availability.presentation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.availability.presentation.state.AvailabilityContentState
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider.availableTimes
import com.ianarbuckle.gymplanner.android.utils.DataProvider.daysOfWeek
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined
import com.ianarbuckle.gymplanner.availability.domain.Time
import kotlinx.collections.immutable.toImmutableList

@Suppress("LongParameterList")
@Composable
fun AvailabilityContent(
    paddingValues: PaddingValues,
    contentState: AvailabilityContentState,
    calendarPagerState: PagerState,
    timeSlotPagerState: PagerState,
    verticalScrollState: ScrollState,
    onSelectedDateChange: (String) -> Unit,
    onTimeSlotChange: (String, String) -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(verticalScrollState).padding(paddingValues)) {
        PersonalTrainerCard(
            personalTrainerLabel = contentState.personalTrainerLabel,
            name = contentState.name,
            imageUrl = contentState.imageUrl,
            qualifications = contentState.qualifications,
            isAvailable = contentState.isAvailable,
        )

        CalendarPickerCard(
            daysOfWeek = contentState.daysOfWeek,
            availableTimes = contentState.availableTimes,
            calendarPagerState = calendarPagerState,
            timeSlotPagerState = timeSlotPagerState,
            timeslotRowsPerPage = contentState.timeslotRowsPerPage,
            timeslotItemsPerPage = contentState.timeslotItemsPerPage,
            onTimeSlotClick = { id, time -> onTimeSlotChange(id, time) },
            selectedDate = contentState.selectedDate,
            onSelectedDateChange = { onSelectedDateChange(it) },
            selectedTimeSlot = contentState.selectedTimeSlot,
            modifier = Modifier,
        )
    }

    if (contentState.selectedTimeSlot.isNotEmpty()) {
        BookNowButton(onBookClick = onBookClick)
    }
}

@Composable
fun BookNowButton(onBookClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = { onBookClick() },
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(16.dp),
        ) {
            Text(text = "Book appointment")
        }
    }
}

@Suppress("MagicNumber")
@OptIn(ExperimentalMaterial3Api::class)
@PreviewsCombined
@Composable
private fun BookingContentPreview() {
    val timeSlots =
        availableTimes
            .map { Time(id = it, startTime = it, endTime = it, status = "AVAILABLE") }
            .toImmutableList()

    val timeslotPickerPageSize = 3
    val calendarPagerSize = 4
    val pagerOffset = 5

    val contentState =
        AvailabilityContentState(
            personalTrainerLabel = "Personal Trainer",
            name = "John Doe",
            imageUrl = "https://randomuser.me/api/port",
            qualifications =
                listOf("Certified Personal Trainer", "Certified Nutritionist").toImmutableList(),
            daysOfWeek = daysOfWeek,
            availableTimes = timeSlots,
            isAvailable = true,
            selectedDate = "2024-12-09",
            selectedTimeSlot = "06:00 AM",
            timeslotRowsPerPage = timeslotPickerPageSize,
            timeslotItemsPerPage = timeslotPickerPageSize,
        )

    GymAppTheme {
        Scaffold(topBar = { TopAppBar(title = { Text("Westwood Gym") }) }) { innerPadding ->
            AvailabilityContent(
                paddingValues = innerPadding,
                contentState = contentState,
                calendarPagerState =
                    rememberPagerState { (daysOfWeek.size + calendarPagerSize) / pagerOffset },
                timeSlotPagerState =
                    rememberPagerState {
                        availableTimes.chunked(timeslotPickerPageSize * timeslotPickerPageSize).size
                    },
                verticalScrollState = rememberScrollState(),
                onSelectedDateChange = {},
                onTimeSlotChange = { _, _ -> },
                onBookClick = {},
            )
        }
    }
}
