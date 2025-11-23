package com.ianarbuckle.gymplanner.android.booking

import android.Manifest
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ianarbuckle.gymplanner.android.MainActivity
import com.ianarbuckle.gymplanner.android.booking.fakes.FakeBookingRepository
import com.ianarbuckle.gymplanner.android.booking.robot.BookingRobot
import com.ianarbuckle.gymplanner.android.booking.verifier.BookingVerifier
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.android.login.robot.LoginRobot
import com.ianarbuckle.gymplanner.android.utils.ComposeIdlingResource
import com.ianarbuckle.gymplanner.android.utils.ConditionalPermissionRule
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.DisableAnimationsRule
import com.ianarbuckle.gymplanner.android.utils.FakeDataStore
import com.ianarbuckle.gymplanner.android.utils.KoinTestRule
import com.ianarbuckle.gymplanner.android.utils.currentWeekDates
import com.ianarbuckle.gymplanner.booking.BookingRepository
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BookingInstrumentedTests {

    @get:Rule(order = 1) val disableAnimationsRule = DisableAnimationsRule()

    @get:Rule(order = 2) val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 3) val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 4)
    val postNotificationsPermissionRule =
        ConditionalPermissionRule(permission = Manifest.permission.POST_NOTIFICATIONS, minSdk = 33)

    private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }

    @get:Rule val koinTestRule = KoinTestRule(modules = listOf(testModule))

    @BindValue @JvmField val dashboardViewModel = mockk<DashboardViewModel>(relaxed = true)

    @BindValue @JvmField val gymLocationsViewModel = mockk<GymLocationsViewModel>(relaxed = true)

    @BindValue @JvmField val bookingViewModel = mockk<BookingViewModel>(relaxed = true)

    @Inject lateinit var bookingRepository: BookingRepository

    private val fakeBookingRepository: FakeBookingRepository
        get() = bookingRepository as FakeBookingRepository

    private val loginRobot = LoginRobot(composeTestRule)

    private val bookingRobot: BookingRobot = BookingRobot(composeTestRule)

    private val bookingVerifier: BookingVerifier = BookingVerifier(composeTestRule)

    private val composeIdleResource = ComposeIdlingResource()

    @Before
    fun setUp() {
        hiltTestRule.inject()

        IdlingRegistry.getInstance().register(composeIdleResource)

        // Correct the static function reference
        mockkStatic("com.ianarbuckle.gymplanner.android.utils.ListExtKt")
        // Correct the static function reference
        mockkStatic("com.ianarbuckle.gymplanner.android.utils.LocalTimeExtKt")

        // Define the behavior of the mocked function
        every { currentWeekDates() } returns DataProvider.daysOfWeek
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(composeIdleResource)
    }

    @Test
    fun testBookingIsSuccessfulWhenEndUserConfirms() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { dashboardViewModel.uiState.value } returns
            DashboardUiState.Success(
                items = DataProvider.fitnessClasses(),
                profile = DataProvider.profile(),
                booking = DataProvider.bookings(),
            )

        coEvery { gymLocationsViewModel.uiState.value } returns
            GymLocationsUiState.Success(gymLocations = DataProvider.gymLocations())

        val bookingUiStateFlow = MutableStateFlow<BookingUiState>(BookingUiState.Idle)
        every { bookingViewModel.bookingUiState } returns bookingUiStateFlow

        bookingRobot.apply {
            clickOnPersonalTrainersNavTab()
            clickOnGymLocation(0)
            clickOnBookPersonalTrainer()
            clickOnTimeSlotDay(0)
            clickOnTimeSlot(0)
            clickOnBookAppointment()
            clickOnConfirmAppointment()
        }

        bookingUiStateFlow.update {
            BookingUiState.Success(booking = DataProvider.bookingResponse())
        }

        bookingVerifier.verifyBookingIsSuccessful()
    }

    @Test
    fun testBookingHasFailedWhenEndUserConfirms() {
        fakeBookingRepository.shouldReturnError = true

        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { dashboardViewModel.uiState.value } returns
            DashboardUiState.Success(
                items = DataProvider.fitnessClasses(),
                profile = DataProvider.profile(),
                booking = DataProvider.bookings(),
            )

        coEvery { gymLocationsViewModel.uiState.value } returns
            GymLocationsUiState.Success(gymLocations = DataProvider.gymLocations())

        val bookingUiStateFlow = MutableStateFlow<BookingUiState>(BookingUiState.Idle)
        every { bookingViewModel.bookingUiState } returns bookingUiStateFlow

        bookingRobot.apply {
            clickOnPersonalTrainersNavTab()
            clickOnGymLocation(0)
            clickOnBookPersonalTrainer()
            clickOnTimeSlotDay(0)
            clickOnTimeSlot(0)
            clickOnBookAppointment()
            clickOnConfirmAppointment()
        }

        bookingUiStateFlow.update { BookingUiState.Failed }

        bookingVerifier.verifyBookingFailed()
    }
}
