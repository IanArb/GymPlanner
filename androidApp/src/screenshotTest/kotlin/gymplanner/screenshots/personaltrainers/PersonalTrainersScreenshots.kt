package gymplanner.screenshots.personaltrainers

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.ianarbuckle.gymplanner.android.personaltrainers.presentation.PersonalTrainerItem
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import gymplanner.screenshots.ScreenshotPreview
import gymplanner.screenshots.ScreenshotPreviews

@PreviewTest
@ScreenshotPreviews
@Composable
private fun PersonalTrainerItemScreenshot() {
    ScreenshotPreview {
        PersonalTrainerItem(
            personalTrainer = DataProvider.personalTrainers().first(),
            onSocialLinkClick = {},
            onBookTrainerClick = {},
            onItemClick = {},
        )
    }
}
