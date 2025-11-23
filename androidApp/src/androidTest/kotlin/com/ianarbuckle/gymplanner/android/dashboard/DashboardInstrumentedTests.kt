package com.ianarbuckle.gymplanner.android.dashboard

import android.Manifest
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ianarbuckle.gymplanner.android.MainActivity
import com.ianarbuckle.gymplanner.android.booking.fakes.FakeBookingRepository
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.dashboard.fakes.FakeFitnessClassRepository
import com.ianarbuckle.gymplanner.android.dashboard.fakes.FakeProfileRepository
import com.ianarbuckle.gymplanner.android.dashboard.verifier.DashboardVerifier
import com.ianarbuckle.gymplanner.android.login.robot.LoginRobot
import com.ianarbuckle.gymplanner.android.utils.ConditionalPermissionRule
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.FakeDataStore
import com.ianarbuckle.gymplanner.android.utils.KoinTestRule
import com.ianarbuckle.gymplanner.booking.BookingRepository
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.profile.ProfileRepository
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DashboardInstrumentedTests {

    @get:Rule(order = 1) val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2) val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 3)
    val postNotificationsPermissionRule =
        ConditionalPermissionRule(permission = Manifest.permission.POST_NOTIFICATIONS, minSdk = 33)

    private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }

    @get:Rule val koinTestRule = KoinTestRule(modules = listOf(testModule))

    @BindValue @JvmField val viewModel = mockk<DashboardViewModel>(relaxed = true)

    @Inject lateinit var fitnessClassRepository: FitnessClassRepository

    private val fakeFitnessClassRepository: FakeFitnessClassRepository
        get() = fitnessClassRepository as FakeFitnessClassRepository

    @Inject lateinit var profileRepository: ProfileRepository

    private val fakeProfileRepository: FakeProfileRepository
        get() = profileRepository as FakeProfileRepository

    @Inject lateinit var bookingsRepository: BookingRepository

    private val fakeBookingsRepository: FakeBookingRepository
        get() = bookingsRepository as FakeBookingRepository

    private val loginRobot = LoginRobot(composeTestRule)
    private val dashboardVerifier = DashboardVerifier(composeTestRule)

    @Before
    fun setup() {
        hiltTestRule.inject()
    }

    @Test
    fun verifyDashboardSuccessState() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { viewModel.uiState.value } returns
            DashboardUiState.Success(
                items = DataProvider.fitnessClasses().toImmutableList(),
                profile = DataProvider.profile(),
                booking = persistentListOf(),
            )

        dashboardVerifier.apply {
            verifyBookPersonalTrainerTextExists()
            verifyFitnessClassesTextExists()
        }
    }

    @Test
    fun verifyDashboardSuccessStateWhenBookingsFailed() {
        fakeBookingsRepository.shouldReturnError = false

        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { viewModel.uiState.value } returns
            DashboardUiState.Success(
                items = DataProvider.fitnessClasses().toImmutableList(),
                profile = DataProvider.profile(),
                booking = persistentListOf(),
            )

        dashboardVerifier.apply {
            verifyBookPersonalTrainerTextExists()
            verifyFitnessClassesTextExists()
        }
    }

    @Test
    fun verifyDashboardSuccessStateWithBookings() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { viewModel.uiState.value } returns
            DashboardUiState.Success(
                items = DataProvider.fitnessClasses().toImmutableList(),
                profile = DataProvider.profile(),
                booking = DataProvider.bookings(),
            )

        dashboardVerifier.apply { verifyUserBookings() }
    }

    @Test
    fun verifyDashboardErrorState() {
        fakeFitnessClassRepository.shouldReturnError = true
        fakeProfileRepository.shouldReturnError = true

        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { viewModel.uiState.value } returns DashboardUiState.Failure

        dashboardVerifier.apply { verifyErrorState() }
    }
}
