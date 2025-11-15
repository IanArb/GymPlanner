package com.ianarbuckle.gymplanner.android.gymlocations

import android.Manifest
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ianarbuckle.gymplanner.android.MainActivity
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.android.gymlocations.robot.GymLocationsRobot
import com.ianarbuckle.gymplanner.android.gymlocations.verifier.GymLocationsVerifier
import com.ianarbuckle.gymplanner.android.login.robot.LoginRobot
import com.ianarbuckle.gymplanner.android.utils.ConditionalPermissionRule
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.FakeDataStore
import com.ianarbuckle.gymplanner.android.utils.KoinTestRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class GymLocationsInstrumentedTests {

    @get:Rule(order = 1) val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2) val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 3)
    val postNotificationsPermissionRule =
        ConditionalPermissionRule(permission = Manifest.permission.POST_NOTIFICATIONS, minSdk = 33)

    private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }

    @get:Rule val koinTestRule = KoinTestRule(modules = listOf(testModule))

    @BindValue @JvmField val gymLocationsViewModel = mockk<GymLocationsViewModel>(relaxed = true)

    @BindValue @JvmField val dashboardViewModel = mockk<DashboardViewModel>(relaxed = true)

    private val loginRobot = LoginRobot(composeTestRule)
    private val gymLocationsRobot = GymLocationsRobot(composeTestRule)
    private val gymLocationsVerifier = GymLocationsVerifier(composeTestRule)

    @Test
    fun verifyGymLocationsSuccessState() {
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
            GymLocationsUiState.Success(DataProvider.gymLocations())

        gymLocationsRobot.apply { tapOnGymLocationsNavTab() }

        gymLocationsVerifier.apply {
            verifyGymLocationsScreenIsDisplayed()
            verifyGymLocationsItemsSize(6)
        }
    }

    @Test
    fun verifyGymLocationsErrorState() {
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

        coEvery { gymLocationsViewModel.uiState.value } returns GymLocationsUiState.Failure

        gymLocationsRobot.apply { tapOnGymLocationsNavTab() }

        gymLocationsVerifier.apply {
            verifyGymLocationsScreenIsDisplayed()
            verifyErrorStateIsDisplayed()
        }
    }
}
