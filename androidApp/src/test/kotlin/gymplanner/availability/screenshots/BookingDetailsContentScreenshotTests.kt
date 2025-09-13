package gymplanner.availability.screenshots

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.ianarbuckle.gymplanner.android.booking.presentation.BookingDetailsContent
import gymplanner.utils.FakeDataStore
import gymplanner.utils.KoinTestRule
import gymplanner.utils.ScreenTestPreview
import gymplanner.utils.createComposeTestRule
import gymplanner.utils.createRoborazziRule
import kotlinx.datetime.LocalTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class BookingDetailsContentScreenshotTests {

    @get:Rule val roborazziRule: RoborazziRule = createRoborazziRule()

    @get:Rule
    val composeTestRule:
        AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity> =
        createComposeTestRule<ComponentActivity>()

    private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }

    @get:Rule val koinTestRule = KoinTestRule(listOf(testModule))

    @Test
    fun verify_booking_details_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            ScreenTestPreview {
                Surface {
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
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun verify_booking_details_is_displayed_correctly_in_dark_mode() {
        composeTestRule.setContent {
            ScreenTestPreview(isDarkTheme = true) {
                Surface {
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
        }

        composeTestRule.onRoot().captureRoboImage()
    }
}
