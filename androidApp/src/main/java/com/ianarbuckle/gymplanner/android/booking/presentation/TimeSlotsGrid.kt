package com.ianarbuckle.gymplanner.android.booking.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider.availableTimes
import com.ianarbuckle.gymplanner.availability.domain.Time

@Composable
fun TimeSlotsGrid(
    availableTimes: List<Time>,
    onTimeSlotClick: (String) -> Unit,
    selectedTimeSlot: String? = null,
    modifier: Modifier = Modifier,
) {
    val rowsPerPage = 3
    val itemsPerPage = rowsPerPage * 3
    val pages = availableTimes.chunked(itemsPerPage)
    val pagerState = rememberPagerState {
        pages.size
    }

    if (pagerState.pageCount > 1) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            NextPrevIndicators(
                pagerState = pagerState,
            )
        }
    } else {
        Spacer(modifier = modifier.padding(8.dp))
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth()
    ) { pageIndex ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = modifier
                .testTag(AvailableTimesGrid)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pages[pageIndex]) { timeSlot ->
                val borderColor =
                    when {
                        timeSlot.id == selectedTimeSlot -> MaterialTheme.colorScheme.primary
                        timeSlot.status == "BOOKED" || timeSlot.status == "UNAVAILABLE" -> Color.Gray
                        else -> Color.Gray
                    }

                Box(
                    modifier = modifier
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (timeSlot.status == "AVAILABLE") {
                                onTimeSlotClick(timeSlot.id)
                            }
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val slot = timeSlot.startTime

                    val textColor =
                        when {
                            timeSlot.id == selectedTimeSlot -> MaterialTheme.colorScheme.primary
                            timeSlot.status == "BOOKED" || timeSlot.status == "UNAVAILABLE" -> Color.Gray
                            timeSlot.status == "AVAILABLE" -> MaterialTheme.colorScheme.onSurface
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    Text(
                        text = slot,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

const val AvailableTimesGrid = "AvailableTimesGrid"

@Preview(showBackground = true, name = "Light mode")
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TimeSlotsGridPreview() {
    val timeSlots = availableTimes.map {
        Time(
            id = it,
            startTime = it,
            endTime = it,
            status = "AVAILABLE"
        )
    }

    GymAppTheme {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(bottom = 16.dp)
        ) {
            TimeSlotsGrid(
                availableTimes = timeSlots,
                onTimeSlotClick = { },
                selectedTimeSlot = "09:00"
            )
        }
    }
}