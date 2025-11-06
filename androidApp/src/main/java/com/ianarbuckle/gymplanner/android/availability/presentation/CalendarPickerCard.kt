package com.ianarbuckle.gymplanner.android.availability.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider.availableTimes
import com.ianarbuckle.gymplanner.android.utils.DataProvider.daysOfWeek
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined
import com.ianarbuckle.gymplanner.android.utils.currentMonth
import com.ianarbuckle.gymplanner.android.utils.displayTime
import com.ianarbuckle.gymplanner.android.utils.toLocalTime
import com.ianarbuckle.gymplanner.availability.domain.Time
import java.util.Calendar

@Suppress("LongMethod")
@Composable
fun CalendarPickerCard(
    daysOfWeek: List<String>,
    availableTimes: List<Time>,
    calendarPagerState: PagerState,
    timeSlotPagerState: PagerState,
    selectedDate: String,
    selectedTimeSlot: String,
    timeslotRowsPerPage: Int,
    timeslotItemsPerPage: Int,
    onTimeSlotClick: (String, String) -> Unit,
    onSelectedDateChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.fillMaxWidth().padding(16.dp),
    ) {
        Column(modifier = Modifier.height(360.dp).fillMaxWidth().padding(16.dp)) {
            Text(
                text = "Available Time",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val calendarMonth = Calendar.getInstance().currentMonth()
                Text(
                    text = calendarMonth,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
            }

            CalendarWeekDaysRow(
                pagerState = calendarPagerState,
                daysOfWeek = daysOfWeek,
                selectedDate = selectedDate,
                onSelectedDateChange = onSelectedDateChange,
                modifier = Modifier,
            )

            Spacer(modifier = Modifier.padding(2.dp))

            HorizontalDivider()

            TimeSlotsBox(
                availableTimes = availableTimes,
                selectedTimeSlotId = selectedTimeSlot,
                onTimeSlotClick = onTimeSlotClick,
                pagerState = timeSlotPagerState,
                rowsPerPage = timeslotRowsPerPage,
                itemsPerPage = timeslotItemsPerPage,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun TimeSlotsBox(
    availableTimes: List<Time>,
    selectedTimeSlotId: String,
    onTimeSlotClick: (String, String) -> Unit,
    pagerState: PagerState,
    rowsPerPage: Int,
    itemsPerPage: Int,
    modifier: Modifier = Modifier,
) {
    val pages = availableTimes.chunked(itemsPerPage)

    Column(modifier = modifier.fillMaxWidth()) {
        if (pagerState.pageCount > 1) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                NextPrevIndicators(pagerState = pagerState, modifier = Modifier)
            }
        } else {
            Spacer(modifier = Modifier.padding(8.dp))
        }

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { pageIndex ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(rowsPerPage),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.testTag(AvailableTimesGrid).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(pages[pageIndex]) { timeSlot ->
                    val borderColor =
                        when {
                            timeSlot.id == selectedTimeSlotId -> MaterialTheme.colorScheme.primary
                            timeSlot.status == "BOOKED" || timeSlot.status == "UNAVAILABLE" ->
                                Color.Gray
                            else -> Color.Gray
                        }

                    Box(
                        modifier =
                            Modifier.border(
                                    width = 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .clickable {
                                    if (timeSlot.status == "AVAILABLE") {
                                        onTimeSlotClick(timeSlot.id, timeSlot.startTime)
                                    }
                                }
                                .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        val slot = timeSlot.startTime.toLocalTime().displayTime()

                        val textColor =
                            when {
                                timeSlot.id == selectedTimeSlotId ->
                                    MaterialTheme.colorScheme.primary
                                timeSlot.status == "BOOKED" || timeSlot.status == "UNAVAILABLE" ->
                                    Color.Gray
                                timeSlot.status == "AVAILABLE" ->
                                    MaterialTheme.colorScheme.onSurface
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        Text(
                            text = slot,
                            color = textColor,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

const val AvailableTimesGrid = "AvailableTimesGrid"

@PreviewsCombined
@Composable
private fun CalendarPickerCardPreview() {
    GymAppTheme {
        val pagerState = rememberPagerState {
            PageSize // Calculate the number of pages needed
        }

        val timeSlotPagerState = rememberPagerState { PageSize }

        val timeSlots =
            availableTimes.map { Time(id = it, startTime = it, endTime = it, status = "AVAILABLE") }
        Surface {
            CalendarPickerCard(
                daysOfWeek = daysOfWeek,
                availableTimes = timeSlots,
                calendarPagerState = pagerState,
                timeSlotPagerState = timeSlotPagerState,
                selectedDate = "2024-12-12",
                selectedTimeSlot = "09:00 AM",
                timeslotRowsPerPage = 3,
                timeslotItemsPerPage = 9,
                onTimeSlotClick = { _, _ -> },
                onSelectedDateChange = {},
            )
        }
    }
}
