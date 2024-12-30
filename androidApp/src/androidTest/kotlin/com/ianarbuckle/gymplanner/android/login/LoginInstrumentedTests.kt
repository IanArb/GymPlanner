package com.ianarbuckle.gymplanner.android.login

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ianarbuckle.gymplanner.android.MainActivity
import com.ianarbuckle.gymplanner.android.login.robot.LoginRobot
import com.ianarbuckle.gymplanner.android.login.verifier.LoginVerifier
import com.ianarbuckle.gymplanner.android.utils.ComposeIdlingResource
import com.ianarbuckle.gymplanner.android.utils.FakeDataStore
import com.ianarbuckle.gymplanner.android.utils.KoinTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTests {

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

    private val loginRobot = LoginRobot(composeTestRule)
    private val loginVerifier = LoginVerifier(composeTestRule)
    private val composeIdlingResource = ComposeIdlingResource()

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(composeIdlingResource)
        hiltTestRule.inject()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(composeIdlingResource)
    }

    @Test
    fun testSuccessfulLogin() {
        loginRobot.apply {
            enterUsernamePassword("test", "Travelport")
            login()
        }

        loginVerifier.verifyDashboardScreenIsDisplayed("Today's Classes")
    }

    @Test
    fun testLoginWithEmptyFields() {
        loginRobot.login()

        loginVerifier.checkErrorText("Username is required")
        loginVerifier.checkErrorText("Password is required")
    }

    @Test
    fun testLoginWithInvalidCredentials() {
        loginRobot.apply {
            enterUsernamePassword("invaliduser", "wrongpassword")
            login()
        }

        loginVerifier.checkErrorText("Error logging in. Please try again.")
    }
}
