package gymplanner.reporting.screenshots

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.ianarbuckle.gymplanner.android.reporting.presentation.FormFields
import com.ianarbuckle.gymplanner.android.reporting.presentation.FormResponseCard
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import gymplanner.utils.ScreenTestPreview
import gymplanner.utils.createComposeTestRule
import gymplanner.utils.createRoborazziRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ReportingFormContentScreenshotTests {

    @get:Rule
    val roborazziRule: RoborazziRule = createRoborazziRule()

    @get:Rule
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity> =
        createComposeTestRule<ComponentActivity>()

    @Test
    fun verify_reporting_form_fields_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            ScreenTestPreview {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    FormFields(
                        machineNumber = "1",
                        description = "description",
                        isMachineNumberValid = true,
                        isDescriptionValid = true,
                        hasMachineNumberInteracted = true,
                        hasDescriptionInteracted = true,
                        onMachineNumberChange = { },
                        onDescriptionChange = { },
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun verify_reporting_form_fields_with_invalid_fields_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            ScreenTestPreview {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    FormFields(
                        machineNumber = "1",
                        description = "description",
                        isMachineNumberValid = false,
                        isDescriptionValid = false,
                        hasMachineNumberInteracted = true,
                        hasDescriptionInteracted = true,
                        onMachineNumberChange = { },
                        onDescriptionChange = { },
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Config(qualifiers = "+night")
    @Test
    fun verify_reporting_form_fields_is_displayed_correctly_in_dark_mode() {
        composeTestRule.setContent {
            ScreenTestPreview(isDarkTheme = true) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    FormFields(
                        machineNumber = "1",
                        description = "description",
                        isMachineNumberValid = true,
                        isDescriptionValid = true,
                        hasMachineNumberInteracted = true,
                        hasDescriptionInteracted = true,
                        onMachineNumberChange = { },
                        onDescriptionChange = { },
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Config(qualifiers = "+night")
    @Test
    fun verify_reporting_form_fields_with_invalid_fields_is_displayed_correctly_in_dark_mode() {
        composeTestRule.setContent {
            ScreenTestPreview(isDarkTheme = true) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    FormFields(
                        machineNumber = "1",
                        description = "description",
                        isMachineNumberValid = false,
                        isDescriptionValid = false,
                        hasMachineNumberInteracted = true,
                        hasDescriptionInteracted = true,
                        onMachineNumberChange = { },
                        onDescriptionChange = { },
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun verify_form_response_card_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            ScreenTestPreview {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    FormResponseCard(
                        faultReport = FaultReport(
                            machineNumber = 1,
                            description = "description",
                            photoUri = "https://www.example.com/image.jpg",
                            date = "2022-01-01"
                        ),
                        onClick = { }
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun verify_form_response_card_is_displayed_correctly_in_dark_mode() {
        composeTestRule.setContent {
            ScreenTestPreview(isDarkTheme = true) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    FormResponseCard(
                        faultReport = FaultReport(
                            machineNumber = 1,
                            description = "description",
                            photoUri = "https://www.example.com/image.jpg",
                            date = "2022-01-01"
                        ),
                        onClick = { }
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }
}