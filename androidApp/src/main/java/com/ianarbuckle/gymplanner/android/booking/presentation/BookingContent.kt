package com.ianarbuckle.gymplanner.android.booking.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider.availableTimes
import com.ianarbuckle.gymplanner.android.utils.DataProvider.daysOfWeek
import com.ianarbuckle.gymplanner.availability.domain.Time

@Suppress("LongParameterList")
@Composable
fun BookingContent(
    paddingValues: PaddingValues,
    personalTrainerLabel: String,
    name: String,
    imageUrl: String,
    qualifications: List<String>,
    daysOfWeek: List<String>,
    availableTimes: List<Time>,
    selectedDate: String,
    selectedTimeSlot: String,
    isAvailable: Boolean,
    calendarPagerState: PagerState,
    timeSlotPagerState: PagerState,
    verticalScrollState: ScrollState,
    timeslotRowsPerPage: Int,
    timeslotItemsPerPage: Int,
    onSelectedDateChange: (String) -> Unit,
    onTimeSlotChange: (String) -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(verticalScrollState)
            .padding(paddingValues),
    ) {
        PersonalTrainerCard(
            personalTrainerLabel = personalTrainerLabel,
            name = name,
            imageUrl = imageUrl,
            qualifications = qualifications,
            isAvailable = isAvailable,
        )

        CalendarPickerCard(
            daysOfWeek = daysOfWeek,
            availableTimes = availableTimes,
            calendarPagerState = calendarPagerState,
            timeSlotPagerState = timeSlotPagerState,
            timeslotRowsPerPage = timeslotRowsPerPage,
            timeslotItemsPerPage = timeslotItemsPerPage,
            onTimeSlotClick = {
                onTimeSlotChange(it)
            },
            selectedDate = selectedDate,
            onSelectedDateChange = {
                onSelectedDateChange(it)
            },
            selectedTimeSlot = selectedTimeSlot,
            modifier = Modifier,
        )
    }

    if (selectedTimeSlot.isNotEmpty()) {
        BookNowButton(
            onBookClick = onBookClick,
        )
    }
}

@Composable
fun BookNowButton(
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Button(
            onClick = { onBookClick() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        ) {
            Text(text = "Book appointment")
        }
    }
}

@Suppress("MagicNumber")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun BookingContentPreview() {
    val timeSlots = availableTimes.map {
        Time(
            id = it,
            startTime = it,
            endTime = it,
            status = "AVAILABLE",
        )
    }

    val timeslotPickerPageSize = 3
    val calendarPagerSize = 4
    val pagerOffset = 5

    GymAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Westwood Gym")
                    },
                )
            },
        ) { innerPadding ->
            BookingContent(
                paddingValues = innerPadding,
                personalTrainerLabel = "Personal Trainer",
                name = "John Doe",
                imageUrl = "https://randomuser.me/api/port",
                qualifications = listOf("Certified Personal Trainer", "Certified Nutritionist"),
                daysOfWeek = daysOfWeek,
                availableTimes = timeSlots,
                calendarPagerState = rememberPagerState {
                    (daysOfWeek.size + calendarPagerSize) / pagerOffset
                },
                timeSlotPagerState = rememberPagerState {
                    availableTimes.chunked(timeslotPickerPageSize * timeslotPickerPageSize).size
                },
                verticalScrollState = rememberScrollState(),
                timeslotRowsPerPage = timeslotPickerPageSize,
                timeslotItemsPerPage = timeslotPickerPageSize,
                onSelectedDateChange = {
                },
                onTimeSlotChange = {
                },
                isAvailable = true,
                selectedDate = "2024-12-09",
                selectedTimeSlot = "06:00 AM",
                onBookClick = {
                },
            )
        }
    }
}
