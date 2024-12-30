package com.ianarbuckle.gymplanner.android.booking.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    onSelectedDateChange: (String) -> Unit,
    onTimeSlotChange: (String) -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState {
        (daysOfWeek.size + PAGE_SIZE) / PAGE_OFFSET // Calculate the number of pages needed
    }

    val verticalScroll = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(verticalScroll)
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
            pagerState = pagerState,
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
        // Button at the bottom center of the screen
        Button(
            onClick = { onBookClick() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Align to bottom center
                .padding(16.dp), // Add some padding from the bottom
        ) {
            Text(text = "Book appointment")
        }
    }
}

private const val PAGE_SIZE = 4
private const val PAGE_OFFSET = 5

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
