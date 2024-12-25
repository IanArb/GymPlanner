package com.ianarbuckle.gymplanner.android.personaltrainers

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ianarbuckle.gymplanner.android.MainActivity
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.android.login.robot.LoginRobot
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersUiState
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersViewModel
import com.ianarbuckle.gymplanner.android.personaltrainers.robot.PersonalTrainersRobot
import com.ianarbuckle.gymplanner.android.personaltrainers.verifier.PersonalTrainersVerifier
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
class PersonalTrainersInstrumentedTests {

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
    val dashboardViewModel = mockk<DashboardViewModel>(relaxed = true)

    @BindValue
    @JvmField
    val gymLocationsViewModel = mockk<GymLocationsViewModel>(relaxed = true)

    @BindValue
    @JvmField
    val personalTrainersViewModel = mockk<PersonalTrainersViewModel>(relaxed = true)

    private val loginRobot = LoginRobot(composeTestRule)
    private val personalTrainersRobot = PersonalTrainersRobot(composeTestRule)
    private val personalTrainersVerifier = PersonalTrainersVerifier(composeTestRule)

    @Test
    fun testPersonalTrainersSuccessState() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { dashboardViewModel.uiState.value } returns DashboardUiState.Success(
            items = DataProvider.fitnessClasses(),
            profile = DataProvider.profile()
        )

        coEvery { gymLocationsViewModel.uiState.value } returns GymLocationsUiState.Success(
            gymLocations = DataProvider.gymLocations()
        )

        coEvery { personalTrainersViewModel.uiState.value } returns PersonalTrainersUiState.Success(
            personalTrainers = DataProvider.personalTrainers()
        )

        personalTrainersRobot.apply {
            clickOnPersonalTrainersNavTab()
            clickOnGymLocation(0)
        }

        personalTrainersVerifier.apply {
            verifyPersonalTrainersScreenIsDisplayed()
            verifyPersonalTrainerCard(0)
            verifyPersonalTrainersCount(3)
        }
    }

    @Test
    fun testPersonalTrainersErrorState() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { dashboardViewModel.uiState.value } returns DashboardUiState.Success(
            items = DataProvider.fitnessClasses(),
            profile = DataProvider.profile()
        )

        coEvery { gymLocationsViewModel.uiState.value } returns GymLocationsUiState.Success(
            gymLocations = DataProvider.gymLocations()
        )

        coEvery { personalTrainersViewModel.uiState.value } returns PersonalTrainersUiState.Failure

        personalTrainersRobot.apply {
            clickOnPersonalTrainersNavTab()
            clickOnGymLocation(0)
        }

        personalTrainersVerifier.apply {
            verifyErrorState()
        }
    }

    @Test
    fun testPersonalTrainersDetailScreenIsDisplayed() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { dashboardViewModel.uiState.value } returns DashboardUiState.Success(
            items = DataProvider.fitnessClasses(),
            profile = DataProvider.profile()
        )

        coEvery { gymLocationsViewModel.uiState.value } returns GymLocationsUiState.Success(
            gymLocations = DataProvider.gymLocations()
        )

        coEvery { personalTrainersViewModel.uiState.value } returns PersonalTrainersUiState.Success(
            personalTrainers = DataProvider.personalTrainers()
        )

        personalTrainersRobot.apply {
            clickOnPersonalTrainersNavTab()
            clickOnGymLocation(0)
            clickOnPersonalTrainerCard(0)
        }

        personalTrainersVerifier.apply {
            val trainer = DataProvider.personalTrainers().first()
            verifyPersonalTrainerDetail(
                name = trainer.firstName.plus(" ").plus(trainer.lastName),
                description = trainer.bio,
            )
        }
    }
}