package com.ianarbuckle.gymplanner.android.dashboard

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.rule.GrantPermissionRule
import com.ianarbuckle.gymplanner.android.MainActivity
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.dashboard.verifier.DashboardVerifier
import com.ianarbuckle.gymplanner.android.login.robot.LoginRobot
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.FakeDataStore
import com.ianarbuckle.gymplanner.android.utils.KoinTestRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
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
    @get:SdkSuppress(minSdkVersion = 33)
    val permissionRule: GrantPermissionRule? =
        if (Build.VERSION.SDK_INT >= 33) {
            GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }

    private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }

    @get:Rule val koinTestRule = KoinTestRule(modules = listOf(testModule))

    @BindValue @JvmField val viewModel = mockk<DashboardViewModel>(relaxed = true)

    private val loginRobot = LoginRobot(composeTestRule)
    private val dashboardVerifier = DashboardVerifier(composeTestRule)

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
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { viewModel.uiState.value } returns DashboardUiState.Failure

        dashboardVerifier.apply { verifyErrorState() }
    }
}
