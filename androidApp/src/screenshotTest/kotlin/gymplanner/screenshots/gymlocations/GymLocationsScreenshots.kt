package gymplanner.screenshots.gymlocations

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.ianarbuckle.gymplanner.android.gymlocations.presentation.GymLocationCard
import gymplanner.screenshots.ScreenshotPreview
import gymplanner.screenshots.ScreenshotPreviews

@PreviewTest
@ScreenshotPreviews
@Composable
private fun GymLocationCardScreenshot() {
    ScreenshotPreview {
        GymLocationCard(
            imageUrl = "https://www.example.com/image.jpg",
            title = "Title",
            subTitle = "SubTitle",
        )
    }
}
