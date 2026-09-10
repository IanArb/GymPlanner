package gymplanner.screenshots.availability

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.ianarbuckle.gymplanner.android.availability.presentation.CalendarWeekDaysRow
import com.ianarbuckle.gymplanner.android.availability.presentation.PersonalTrainerCard
import com.ianarbuckle.gymplanner.android.availability.presentation.TimeSlotsBox
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.availability.domain.Time
import gymplanner.screenshots.ScreenshotPreview
import gymplanner.screenshots.ScreenshotPreviews
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

private const val TimeSlotPagerSize = 6
private const val RowsPerPage = 3
private const val ItemsPerPage = 9

private val timeSlots =
    DataProvider.availableTimes
        .map { Time(id = it, startTime = it, endTime = it, status = "AVAILABLE") }
        .toImmutableList()

@PreviewTest
@ScreenshotPreviews
@Composable
private fun AvailablePersonalTrainerCardScreenshot() {
    ScreenshotPreview {
        PersonalTrainerCard(
            personalTrainerLabel = "Personal Trainer",
            name = "John Doe",
            imageUrl = "https://example.com/image.jpg",
            qualifications = persistentListOf("Qualification 1", "Qualification 2"),
            isAvailable = true,
        )
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
private fun UnavailablePersonalTrainerCardScreenshot() {
    ScreenshotPreview {
        PersonalTrainerCard(
            personalTrainerLabel = "Personal Trainer",
            name = "John Doe",
            imageUrl = "https://example.com/image.jpg",
            qualifications = persistentListOf("Qualification 1", "Qualification 2"),
            isAvailable = false,
        )
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
private fun CalendarWeekDaysRowScreenshot() {
    val pagerState = rememberPagerState { DataProvider.daysOfWeek.size }

    ScreenshotPreview {
        CalendarWeekDaysRow(
            daysOfWeek = DataProvider.daysOfWeek,
            pagerState = pagerState,
            selectedDate = "2024-12-12",
            onSelectedDateChange = {},
        )
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
private fun TimeSlotsBoxScreenshot() {
    val pagerState = rememberPagerState { TimeSlotPagerSize }

    ScreenshotPreview {
        TimeSlotsBox(
            availableTimes = timeSlots,
            selectedTimeSlotId = timeSlots.first().id,
            pagerState = pagerState,
            rowsPerPage = RowsPerPage,
            itemsPerPage = ItemsPerPage,
            onTimeSlotClick = { _, _ -> },
        )
    }
}
