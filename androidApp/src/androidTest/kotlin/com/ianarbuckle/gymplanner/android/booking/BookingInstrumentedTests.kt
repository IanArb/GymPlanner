package com.ianarbuckle.gymplanner.android.booking

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ianarbuckle.gymplanner.android.MainActivity
import com.ianarbuckle.gymplanner.android.booking.robot.BookingRobot
import com.ianarbuckle.gymplanner.android.booking.verifier.BookingVerifier
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.android.login.robot.LoginRobot
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersUiState
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersViewModel
import com.ianarbuckle.gymplanner.android.utils.ComposeIdlingResource
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.FakeDataStore
import com.ianarbuckle.gymplanner.android.utils.KoinTestRule
import com.ianarbuckle.gymplanner.android.utils.currentMonth
import com.ianarbuckle.gymplanner.android.utils.currentWeekDates
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import java.util.Calendar

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BookingInstrumentedTests {

    @get:Rule(order = 1)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val testModule = module {
        single<DataStore<Preferences>> { FakeDataStore() }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(testModule),
    )

    @BindValue
    @JvmField
    val dashboardViewModel = mockk<DashboardViewModel>(relaxed = true)

    @BindValue
    val gymLocationsViewModel = mockk<GymLocationsViewModel>(relaxed = true)

    @BindValue
    val personalTrainersViewModel = mockk<PersonalTrainersViewModel>(relaxed = true)

    @BindValue
    @JvmField
    val bookingViewModel = mockk<BookingViewModel>(relaxed = true)

    private val bookingRobot = BookingRobot(composeTestRule)
    private val loginRobot = LoginRobot(composeTestRule)
    private val bookingVerifier = BookingVerifier(composeTestRule)

    private val composeIdleResource = ComposeIdlingResource()

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(composeIdleResource)

        // Correct the static function reference
        mockkStatic("com.ianarbuckle.gymplanner.android.utils.DateTimeKtKt")

        // Define the behavior of the mocked function
        every { currentWeekDates() } returns DataProvider.daysOfWeek
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(composeIdleResource)
    }

    @Test
    fun testBookingAvailabilitySuccessState() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { dashboardViewModel.uiState.value } returns DashboardUiState.Success(
            items = DataProvider.fitnessClasses(),
            profile = DataProvider.profile(),
        )

        coEvery { gymLocationsViewModel.uiState.value } returns GymLocationsUiState.Success(
            gymLocations = DataProvider.gymLocations(),
        )

        coEvery { personalTrainersViewModel.uiState.value } returns PersonalTrainersUiState.Success(
            personalTrainers = DataProvider.personalTrainers(),
        )

        coEvery { bookingViewModel.bookingState.value } returns BookingUiState.AvailabilitySuccess(
            availability = DataProvider.availability(),
            isPersonalTrainerAvailable = true,
        )

        bookingRobot.apply {
            clickOnPersonalTrainersNavTab()
            clickOnGymLocation(0)
            clickOnBookPersonalTrainer()
        }

        val calendarMonth = Calendar.getInstance().currentMonth()

        bookingVerifier.apply {
            val trainer = DataProvider.personalTrainers().first()
            verifyCalendarMonth(month = calendarMonth)
            verifyPersonalTrainerNameAndDescription(
                name = trainer.firstName.plus(" ").plus(trainer.lastName),
                qualifications = trainer.qualifications,
            )
            verifyAvailableTimesSize(9)
            verifyCalendarGridSize(5)
        }
    }

    @Test
    fun testBookingAvailabilityFailedState() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { dashboardViewModel.uiState.value } returns DashboardUiState.Success(
            items = DataProvider.fitnessClasses(),
            profile = DataProvider.profile(),
        )

        coEvery { gymLocationsViewModel.uiState.value } returns GymLocationsUiState.Success(
            gymLocations = DataProvider.gymLocations(),
        )

        coEvery { personalTrainersViewModel.uiState.value } returns PersonalTrainersUiState.Success(
            personalTrainers = DataProvider.personalTrainers(),
        )

        coEvery { bookingViewModel.bookingState.value } returns BookingUiState.Failed

        bookingRobot.apply {
            clickOnPersonalTrainersNavTab()
            clickOnGymLocation(0)
            clickOnBookPersonalTrainer()
        }

        bookingVerifier.apply {
            verifyErrorState()
        }
    }
}
