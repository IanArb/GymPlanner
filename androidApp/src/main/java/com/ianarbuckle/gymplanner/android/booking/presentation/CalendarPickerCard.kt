package com.ianarbuckle.gymplanner.android.booking.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider.availableTimes
import com.ianarbuckle.gymplanner.android.utils.DataProvider.daysOfWeek
import com.ianarbuckle.gymplanner.availability.domain.Time

@Composable
fun CalendarPickerCard(
    daysOfWeek: List<String>,
    availableTimes: List<Time>,
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    selectedDate: String,
    selectedTimeSlot: String,
    onTimeSlotClick: (String) -> Unit,
    onSelectedDateChange: (String) -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .height(360.dp)
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            CalendarHeader(
                daysOfWeek = daysOfWeek,
                pagerState = pagerState,
                selectedDate = selectedDate,
                onSelectedDateChange = onSelectedDateChange,
            )

            TimeSlotsGrid(
                availableTimes,
                onTimeSlotClick,
                selectedTimeSlot = selectedTimeSlot
            )
        }
    }

}

@Preview(showBackground = true, name = "Light mode")
@Preview(showBackground = true, name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CalendarPickerCardPreview() {
    GymAppTheme {
        val pagerState = rememberPagerState {
            5 // Calculate the number of pages needed
        }
        val timeSlots = availableTimes.map {
            Time(
                id = it,
                startTime = it,
                endTime = it,
                status = "AVAILABLE"
            )
        }
        Surface {
            CalendarPickerCard(
                daysOfWeek = daysOfWeek,
                availableTimes = timeSlots,
                pagerState = pagerState,
                selectedDate = "2024-12-12",
                selectedTimeSlot = "09:00 AM",
                onTimeSlotClick = { },
                onSelectedDateChange = { }
            )
        }
    }
}