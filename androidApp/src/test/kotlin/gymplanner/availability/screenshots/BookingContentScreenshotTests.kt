package gymplanner.availability.screenshots

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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.ianarbuckle.gymplanner.android.availability.presentation.CalendarWeekDaysRow
import com.ianarbuckle.gymplanner.android.availability.presentation.PersonalTrainerCard
import com.ianarbuckle.gymplanner.android.availability.presentation.TimeSlotsBox
import com.ianarbuckle.gymplanner.availability.domain.Time
import gymplanner.utils.FakeDataStore
import gymplanner.utils.KoinTestRule
import gymplanner.utils.ScreenTestPreview
import gymplanner.utils.createComposeTestRule
import gymplanner.utils.createRoborazziRule
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class BookingContentScreenshotTests {

    @get:Rule val roborazziRule: RoborazziRule = createRoborazziRule()

    @get:Rule
    val composeTestRule:
        AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity> =
        createComposeTestRule<ComponentActivity>()

    private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }

    @get:Rule val koinTestRule = KoinTestRule(listOf(testModule))

    @Test
    fun verify_booking_trainers_card_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            ScreenTestPreview {
                Column {
                    PersonalTrainerCard(
                        personalTrainerLabel = "Personal Trainer",
                        name = "John Doe",
                        imageUrl = "https://example.com/image.jpg",
                        qualifications = persistentListOf("Qualification 1", "Qualification 2"),
                        isAvailable = true,
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
                Column {
                    PersonalTrainerCard(
                        personalTrainerLabel = "Personal Trainer",
                        name = "John Doe",
                        imageUrl = "https://example.com/image.jpg",
                        qualifications = persistentListOf("Qualification 1", "Qualification 2"),
                        isAvailable = false,
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
                        qualifications = persistentListOf("Qualification 1", "Qualification 2"),
                        isAvailable = true,
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
                        qualifications = persistentListOf("Qualification 1", "Qualification 2"),
                        isAvailable = false,
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun verify_booking_calendar_header_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            val pagerState = rememberPagerState { 7 }
            ScreenTestPreview {
                Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
                    CalendarWeekDaysRow(
                        daysOfWeek = daysOfWeek,
                        pagerState = pagerState,
                        selectedDate = "2024-12-12",
                        onSelectedDateChange = {},
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
            val pagerState = rememberPagerState { 7 }
            ScreenTestPreview(isDarkTheme = true) {
                Column(
                    Modifier.background(MaterialTheme.colorScheme.surface).padding(bottom = 16.dp)
                ) {
                    CalendarWeekDaysRow(
                        daysOfWeek = daysOfWeek,
                        pagerState = pagerState,
                        selectedDate = "2024-12-12",
                        onSelectedDateChange = {},
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun verify_booking_calendar_time_slots_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            val pagerState = rememberPagerState {
                TimeSlotPagerSize // Calculate the number of pages needed
            }

            ScreenTestPreview {
                Column(
                    Modifier.background(MaterialTheme.colorScheme.surface).padding(bottom = 16.dp)
                ) {
                    TimeSlotsBox(
                        availableTimes = timeSlots,
                        selectedTimeSlotId = "1",
                        pagerState = pagerState,
                        rowsPerPage = RowsPerPage,
                        itemsPerPage = ItemsPerPage,
                        onTimeSlotClick = { _, _ -> },
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
            val pagerState = rememberPagerState {
                TimeSlotPagerSize // Calculate the number of pages needed
            }

            ScreenTestPreview(isDarkTheme = true) {
                Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
                    TimeSlotsBox(
                        availableTimes = timeSlots,
                        selectedTimeSlotId = "2",
                        pagerState = pagerState,
                        rowsPerPage = RowsPerPage,
                        itemsPerPage = ItemsPerPage,
                        onTimeSlotClick = { _, _ -> },
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    companion object {
        val daysOfWeek: ImmutableList<String> =
            persistentListOf(
                "2024-12-08",
                "2024-12-09",
                "2024-12-10",
                "2024-12-11",
                "2024-12-12",
                "2024-12-13",
                "2024-12-14",
            )

        private val availableTimes: ImmutableList<String> =
            persistentListOf(
                "06:00:00",
                "06:30:00",
                "07:00:00",
                "07:30:00",
                "08:00:00",
                "08:30:00",
                "09:00:00",
                "10:00:00",
                "10:30:00",
                "11:00:00",
                "12:00:00",
                "12:30:00",
            )

        val timeSlots =
            availableTimes
                .map { Time(id = it, startTime = it, endTime = it, status = "AVAILABLE") }
                .toImmutableList()

        const val TimeSlotPagerSize = 6
        const val RowsPerPage = 3
        const val ItemsPerPage = 9
    }
}
