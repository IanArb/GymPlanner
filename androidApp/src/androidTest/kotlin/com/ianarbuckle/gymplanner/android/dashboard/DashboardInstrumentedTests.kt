package com.ianarbuckle.gymplanner.android.dashboard

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DashboardInstrumentedTests {

    @get:Rule(order = 1)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val testModule = module {
        single<DataStore<Preferences>> { FakeDataStore()  }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(testModule)
    )

    @BindValue
    @JvmField
    val viewModel = mockk<DashboardViewModel>(relaxed = true)

    private val loginRobot = LoginRobot(composeTestRule)
    private val dashboardVerifier = DashboardVerifier(composeTestRule)

    @Test
    fun verifyDashboardSuccessState() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { viewModel.uiState.value } returns DashboardUiState.Success(
            items = DataProvider.fitnessClasses().toImmutableList(),
            profile = DataProvider.profile()
        )

        dashboardVerifier.apply {
            verifyBookPersonalTrainerTextExists()
            verifyFitnessClassesTextExists()
        }
    }

    @Test
    fun verifyDashboardErrorState() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { viewModel.uiState.value } returns DashboardUiState.Failure

        dashboardVerifier.apply {
            verifyErrorState()
        }
    }
}