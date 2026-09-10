package gymplanner.screenshots.availability

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.ianarbuckle.gymplanner.android.booking.presentation.BookingDetailsContent
import gymplanner.screenshots.ScreenshotPreview
import gymplanner.screenshots.ScreenshotPreviews
import kotlinx.datetime.LocalTime

@PreviewTest
@ScreenshotPreviews
@Composable
private fun BookingDetailsContentScreenshot() {
    ScreenshotPreview {
        BookingDetailsContent(
            selectedDate = "2022-01-01",
            selectedTimeSlot = LocalTime.parse("10:00:00"),
            location = "Clontarf",
            onConfirmClick = {},
            personalTrainerName = "John Joe",
            personalTrainerAvatarUrl = "https://example.com/avatar.jpg",
        )
    }
}
