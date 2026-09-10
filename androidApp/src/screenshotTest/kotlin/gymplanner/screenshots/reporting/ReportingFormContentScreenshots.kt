package gymplanner.screenshots.reporting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.ianarbuckle.gymplanner.android.reporting.presentation.FormFields
import com.ianarbuckle.gymplanner.android.reporting.presentation.FormResponseCard
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import gymplanner.screenshots.ScreenshotPreview
import gymplanner.screenshots.ScreenshotPreviews

@PreviewTest
@ScreenshotPreviews
@Composable
private fun ValidFormFieldsScreenshot() {
    ScreenshotPreview {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            FormFields(
                machineNumber = "1",
                description = "description",
                isMachineNumberValid = true,
                isDescriptionValid = true,
                hasMachineNumberInteracted = true,
                hasDescriptionInteracted = true,
                onMachineNumberChange = {},
                onDescriptionChange = {},
            )
        }
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
private fun InvalidFormFieldsScreenshot() {
    ScreenshotPreview {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            FormFields(
                machineNumber = "1",
                description = "description",
                isMachineNumberValid = false,
                isDescriptionValid = false,
                hasMachineNumberInteracted = true,
                hasDescriptionInteracted = true,
                onMachineNumberChange = {},
                onDescriptionChange = {},
            )
        }
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
private fun FormResponseCardScreenshot() {
    ScreenshotPreview {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            FormResponseCard(
                faultReport =
                FaultReport(
                    machineNumber = 1,
                    description = "description",
                    photoUri = "https://www.example.com/image.jpg",
                    date = "2022-01-01",
                ),
                onClick = {},
            )
        }
    }
}
