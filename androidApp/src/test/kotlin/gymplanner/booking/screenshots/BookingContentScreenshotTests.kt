package gymplanner.booking.screenshots

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.ianarbuckle.gymplanner.android.booking.presentation.CalendarHeader
import com.ianarbuckle.gymplanner.android.booking.presentation.PersonalTrainerCard
import com.ianarbuckle.gymplanner.android.booking.presentation.TimeSlotsGrid
import com.ianarbuckle.gymplanner.availability.domain.Time
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
class BookingContentScreenshotTests {

    @get:Rule
    val roborazziRule: RoborazziRule = createRoborazziRule()

    @get:Rule
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity> =
        createComposeTestRule<ComponentActivity>()

    @Test
    fun verify_booking_trainers_card_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            ScreenTestPreview {
                Surface {
                    PersonalTrainerCard(
                        personalTrainerLabel = "Personal Trainer",
                        name = "John Doe",
                        imageUrl = "https://example.com/image.jpg",
                        qualifications = listOf("Qualification 1", "Qualification 2"),
                        isAvailable = true
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun verify_booking_trainers_card_without_available_icon_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            ScreenTestPreview {
                Surface {
                    PersonalTrainerCard(
                        personalTrainerLabel = "Personal Trainer",
                        name = "John Doe",
                        imageUrl = "https://example.com/image.jpg",
                        qualifications = listOf("Qualification 1", "Qualification 2"),
                        isAvailable = false
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Config(qualifiers = "+night")
    @Test
    fun verify_booking_trainers_card_is_displayed_correctly_in_dark_mode() {
        composeTestRule.setContent {
            ScreenTestPreview(isDarkTheme = true) {
                Surface {
                    PersonalTrainerCard(
                        personalTrainerLabel = "Personal Trainer",
                        name = "John Doe",
                        imageUrl = "https://example.com/image.jpg",
                        qualifications = listOf("Qualification 1", "Qualification 2"),
                        isAvailable = true
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Config(qualifiers = "+night")
    @Test
    fun verify_booking_trainers_card_without_available_icon_is_displayed_correctly_in_dark_mode() {
        composeTestRule.setContent {
            ScreenTestPreview(isDarkTheme = true) {
                Surface {
                    PersonalTrainerCard(
                        personalTrainerLabel = "Personal Trainer",
                        name = "John Doe",
                        imageUrl = "https://example.com/image.jpg",
                        qualifications = listOf("Qualification 1", "Qualification 2"),
                        isAvailable = false
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun verify_booking_calendar_header_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            val pagerState = rememberPagerState {
                7
            }
            ScreenTestPreview {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                )  {
                    CalendarHeader(
                        daysOfWeek = daysOfWeek,
                        pagerState = pagerState,
                        selectedDate = "2024-12-12",
                        onSelectedDateChange = { },
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Config(qualifiers = "+night")
    @Test
    fun verify_booking_calendar_header_is_displayed_correctly_in_dark_mode() {
        composeTestRule.setContent {
            val pagerState = rememberPagerState {
                7
            }
            ScreenTestPreview(isDarkTheme = true) {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(bottom = 16.dp)
                ){
                    CalendarHeader(
                        daysOfWeek = daysOfWeek,
                        pagerState = pagerState,
                        selectedDate = "2024-12-12",
                        onSelectedDateChange = { },
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun verify_booking_calendar_time_slots_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            ScreenTestPreview {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(bottom = 16.dp)
                ) {
                    TimeSlotsGrid(
                        availableTimes = timeSlots,
                        selectedTimeSlot = "07:00 AM",
                        onTimeSlotClick = {}
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Config(qualifiers = "+night")
    @Test
    fun verify_booking_calendar_time_slots_is_displayed_correctly_in_dark_mode() {
        composeTestRule.setContent {
            ScreenTestPreview(isDarkTheme = true) {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    TimeSlotsGrid(
                        availableTimes = timeSlots,
                        selectedTimeSlot = "07:00 AM",
                        onTimeSlotClick = {}
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    companion object {
        val daysOfWeek: List<String> = listOf(
            "2024-12-08", "2024-12-09", "2024-12-10",
            "2024-12-11", "2024-12-12", "2024-12-13", "2024-12-14"
        )

        private val availableTimes: List<String> = listOf(
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
    }

}