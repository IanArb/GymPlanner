package gymplanner.screenshots.dashboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.ianarbuckle.gymplanner.android.dashboard.presentation.BookPersonalTrainerCard
import com.ianarbuckle.gymplanner.android.dashboard.presentation.GymClassesCarousel
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import gymplanner.screenshots.ScreenshotPreview
import gymplanner.screenshots.ScreenshotPreviews

@PreviewTest
@ScreenshotPreviews
@Composable
private fun BookPersonalTrainerCardScreenshot() {
    ScreenshotPreview { BookPersonalTrainerCard(onBookPersonalTrainerClick = {}) }
}

@PreviewTest
@ScreenshotPreviews
@Composable
private fun GymClassesCarouselScreenshot() {
    ScreenshotPreview(modifier = Modifier.fillMaxWidth().height(350.dp)) {
        GymClassesCarousel(classesCarouselItems = DataProvider.fitnessClasses())
    }
}
