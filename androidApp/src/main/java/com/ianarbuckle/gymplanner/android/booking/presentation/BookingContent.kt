package com.ianarbuckle.gymplanner.android.booking.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.common.Avatar
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.convertDate
import com.ianarbuckle.gymplanner.android.utils.currentMonth
import com.ianarbuckle.gymplanner.android.utils.isCurrentDay
import com.ianarbuckle.gymplanner.availability.domain.Time
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun BookingContent(
    paddingValues: PaddingValues,
    personalTrainerLabel: String,
    name: String,
    imageUrl: String,
    qualifications: List<String>,
    daysOfWeek: List<String>,
    availableTimes: List<Time>,
    modifier: Modifier = Modifier,
    selectedDate: String,
    selectedTimeSlot: String,
    isAvailable: Boolean,
    onSelectedDateChange: (String) -> Unit,
    onTimeSlotChange: (String) -> Unit,
    onBookClick: () -> Unit,
) {
    val pagerState = rememberPagerState {
        (daysOfWeek.size + 4) / 5 // Calculate the number of pages needed
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
            modifier = modifier
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
        )
    }

    if (selectedTimeSlot.isNotEmpty()) {
        BookNowButton {
            onBookClick()
        }
    }
}

@Composable
fun PersonalTrainerCard(
    personalTrainerLabel: String,
    name: String,
    imageUrl: String,
    qualifications: List<String>,
    isAvailable: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier
            .padding(16.dp)
    ) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PersonalTrainer(
                personalTrainerLabel = personalTrainerLabel,
                name = name,
                imageUrl = imageUrl,
                qualifications = qualifications,
                isAvailable = isAvailable,
                modifier = modifier
            )
        }

        Spacer(modifier = modifier.padding(8.dp))
    }
}


@Composable
fun BookNowButton(
    onBookClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Button at the bottom center of the screen
        Button(
            onClick = { onBookClick() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Align to bottom center
                .padding(16.dp) // Add some padding from the bottom
        ) {
            Text(text = "Book appointment")
        }
    }
}

@Composable
fun PersonalTrainer(
    personalTrainerLabel: String,
    name: String,
    imageUrl: String,
    qualifications: List<String>,
    isAvailable: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Avatar(
            imageUrl = imageUrl,
            contentDescription = "Avatar",
            isAvailable = isAvailable,
        )

        Spacer(modifier = modifier.padding(2.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = modifier.padding(2.dp))

        Text(
            text = personalTrainerLabel,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = modifier.padding(2.dp))

        Text(
            text = qualifications.joinToString(", "),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            textAlign = TextAlign.Center
        )

    }
}

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

@Composable
fun CalendarHeader(
    daysOfWeek: List<String>,
    pagerState: PagerState,
    selectedDate: String,
    modifier: Modifier = Modifier,
    onSelectedDateChange: (String) -> Unit,
) {
    Text(
        text = "Available Time",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val calendarMonth = Calendar.getInstance().currentMonth()
        Text(
            text = calendarMonth,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            modifier = modifier.weight(1f)
        )
    }

    CalendarWeekDaysRow(
        pagerState = pagerState,
        daysOfWeek = daysOfWeek,
        selectedDate = selectedDate,
        modifier = modifier,
        onSelectedDateChange = onSelectedDateChange,
    )

    Spacer(modifier = Modifier.padding(2.dp))

    HorizontalDivider()
}

@Composable
fun CalendarWeekDaysRow(
    pagerState: PagerState,
    daysOfWeek: List<String>,
    selectedDate: String,
    modifier: Modifier = Modifier,
    onSelectedDateChange: (String) -> Unit,
) {
    Column {
        HorizontalPager(
            state = pagerState
        ) { page ->
            val startIndex = page * 5
            val endIndex = (startIndex + 5).coerceAtMost(daysOfWeek.size)
            val daysSubset = daysOfWeek.subList(startIndex, endIndex)

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(daysSubset) { index, day ->
                    val isCurrentDay = day.isCurrentDay() && selectedDate.isEmpty()

                    Box(
                        modifier = Modifier
                            .clickable {
                                onSelectedDateChange(day)
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val textColor =
                            when {
                                isCurrentDay -> MaterialTheme.colorScheme.primary
                                selectedDate == day -> MaterialTheme.colorScheme.primary
                                else -> Color.Black
                            }
                        val primary = MaterialTheme.colorScheme.primary

                        val formatDay = convertDate(day)

                        Text(
                            text = formatDay,
                            textAlign = TextAlign.Center,
                            color = textColor,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = modifier
                                .drawBehind {
                                    // Draw a line underneath the text
                                    val lineThickness = 2.dp.toPx() // Thickness of the underline
                                    val yOffset = size.height // Line position just below the text
                                    if (isCurrentDay || selectedDate == day) {
                                        drawLine(
                                            color = primary,
                                            start = Offset(
                                                0f,
                                                yOffset
                                            ),
                                            end = Offset(
                                                size.width,
                                                yOffset
                                            ),
                                            strokeWidth = lineThickness
                                        )
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NextPrevIndicators(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    Row {
        IconButton(
            onClick = {
                if (pagerState.currentPage > 0) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous",
                modifier = modifier.size(18.dp)
            )
        }

        IconButton(
            onClick = {
                if (pagerState.currentPage < pagerState.pageCount - 1) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next",
                modifier = modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun TimeSlotsGrid(
    availableTimes: List<Time>,
    onTimeSlotClick: (String) -> Unit,
    selectedTimeSlot: String? = null,
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
        Spacer(modifier = Modifier.padding(8.dp))
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { pageIndex ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth(),
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
                    modifier = Modifier
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
                            timeSlot.status == "AVAILABLE" -> Color.Black
                            else -> Color.Black
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


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BookingContentPreview() {
    val daysOfWeek: List<String> = listOf(
        "2024-12-08", "2024-12-09", "2024-12-10",
        "2024-12-11", "2024-12-12", "2024-12-13", "2024-12-14"
    )

    val availableTimes: List<String> = listOf(
        "06:00 AM", "06:30 AM", "07:00 AM",
        "07:30 AM", "08:00 AM", "08:30 AM",
        "09:00 AM", "10:00 AM", "10:30 AM",
        "11:00 AM", "12:00 PM", "12:30 PM",
    )

    val timeSlots = availableTimes.map {
        Time(
            id = it,
            startTime = it,
            endTime = it,
            status = "AVAILABLE"
        )
    }

    GymAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Westwood Gym")
                    })
            }
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

                }
            )
        }

    }
}