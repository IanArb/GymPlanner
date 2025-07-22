package com.ianarbuckle.gymplanner.android.chat

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ianarbuckle.gymplanner.android.MainActivity
import com.ianarbuckle.gymplanner.android.chat.verifier.ChatVerifier
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.dashboard.robot.DashboardRobot
import com.ianarbuckle.gymplanner.android.login.robot.LoginRobot
import com.ianarbuckle.gymplanner.android.profile.ProfileViewModel
import com.ianarbuckle.gymplanner.android.utils.ComposeIdlingResource
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.DisableAnimationsRule
import com.ianarbuckle.gymplanner.android.utils.FakeDataStore
import com.ianarbuckle.gymplanner.android.utils.KoinTestRule
import com.ianarbuckle.gymplanner.chat.ChatRepository
import com.ianarbuckle.gymplanner.chat.domain.Message
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ChatInstrumentedTests {

  @get:Rule(order = 1) val disableAnimationsRule = DisableAnimationsRule()

  @get:Rule(order = 2) val hiltTestRule = HiltAndroidRule(this)

  @get:Rule(order = 3) val composeTestRule = createAndroidComposeRule<MainActivity>()

  private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }

  @get:Rule val koinTestRule = KoinTestRule(modules = listOf(testModule))

  private val composeIdlingResource = ComposeIdlingResource()

  @BindValue @JvmField val dashboardViewModel = mockk<DashboardViewModel>(relaxed = true)

  @BindValue @JvmField val chatScreenViewModel = mockk<ChatScreenViewModel>(relaxed = true)

  @BindValue @JvmField val profileViewModel = mockk<ProfileViewModel>(relaxed = true)

  @BindValue @JvmField val chatRepository = mockk<ChatRepository>(relaxed = true)

  private val loginRobot = LoginRobot(composeTestRule)

  private val dashboardRobot = DashboardRobot(composeTestRule)

  private val chatVerifier = ChatVerifier(composeTestRule)

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
  fun verifyChatScreenIsDisplayed() {
    loginRobot.apply {
      enterUsernamePassword("test", "password")
      login()
    }

    coEvery { profileViewModel.user.first() } returns Pair("Guest", "userId")

    coEvery { dashboardViewModel.uiState.value } returns
      DashboardUiState.Success(
        items = DataProvider.fitnessClasses(),
        profile = DataProvider.profile(),
        booking = DataProvider.bookings(),
      )

    coEvery { chatScreenViewModel.chatUiState.value } returns
      ChatUiState.MessagesSuccess(
        messages =
          persistentListOf(
              Message(
                text = "Welcome to the chat!",
                username = "System",
                formattedTime = "2025-10-01 12:34:56",
              )
            )
            .toImmutableList()
      )

    dashboardRobot.clickOnChat()

    chatVerifier.apply {
      verifyChatScreenIsDisplayed()
      verifyComposerIsDisplayed()
      verifyMessage("Welcome to the chat!")
    }
  }

}
