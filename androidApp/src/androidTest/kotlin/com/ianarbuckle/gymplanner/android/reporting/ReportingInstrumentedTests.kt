package com.ianarbuckle.gymplanner.android.reporting

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ianarbuckle.gymplanner.android.MainActivity
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.login.robot.LoginRobot
import com.ianarbuckle.gymplanner.android.reporting.data.FormFaultReportUiState
import com.ianarbuckle.gymplanner.android.reporting.data.ReportingViewModel
import com.ianarbuckle.gymplanner.android.reporting.robot.ReportingRobot
import com.ianarbuckle.gymplanner.android.reporting.verifier.ReportingVerifier
import com.ianarbuckle.gymplanner.android.utils.ComposeIdlingResource
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.FakeDataStore
import com.ianarbuckle.gymplanner.android.utils.KoinTestRule
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import java.io.File
import java.io.FileOutputStream
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ReportingInstrumentedTests {

  @get:Rule(order = 1) val hiltTestRule = HiltAndroidRule(this)

  @get:Rule(order = 2) val composeTestRule = createAndroidComposeRule<MainActivity>()

  private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }

  @get:Rule val koinTestRule = KoinTestRule(modules = listOf(testModule))

  val dashboardViewModel = mockk<DashboardViewModel>(relaxed = true)

  val reportingViewModel = mockk<ReportingViewModel>(relaxed = true)

  private val loginRobot = LoginRobot(composeTestRule)
  private val reportingRobot = ReportingRobot(composeTestRule)
  private val reportingVerifier = ReportingVerifier(composeTestRule)

  val idlingResource = ComposeIdlingResource()

  @Before
  fun setup() {
    Intents.init()
    IdlingRegistry.getInstance().register(idlingResource)
  }

  @After
  fun tearDown() {
    Intents.release()
    IdlingRegistry.getInstance().unregister(idlingResource)
  }

  @Test
  fun testReportingScreenWithSuccessfulReport() {
    loginRobot.apply {
      enterUsernamePassword("test", "Travelport")
      login()
    }

    // Create a fake bitmap (this will simulate the photo taken by the camera)
    val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

    // Save the fake bitmap to a file (if you need to use a URI result)
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val fakePhotoFile = File(context.cacheDir, "fake_photo.jpg")
    FileOutputStream(fakePhotoFile).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }

    // Stub the camera intent to return the fake result
    val resultIntent =
      Intent().apply {
        putExtra("data", bitmap) // For camera apps that return a Bitmap in extras
      }
    val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent)

    intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

    coEvery { dashboardViewModel.uiState.value } returns
      DashboardUiState.Success(
        items = DataProvider.fitnessClasses(),
        profile = DataProvider.profile(),
        booking = DataProvider.bookings(),
      )

    reportingRobot.apply {
      tapOnReportNavTab()
      populateForm()
      performPhotoAction()
    }

    reportingRobot.apply { performSend() }

    coEvery { reportingViewModel.submitFault(any()) } returns Unit

    coEvery { reportingViewModel.uiState.value } returns
      FormFaultReportUiState.FormSuccess(
        data =
          FaultReport(
            machineNumber = 123,
            description = "Broken machine",
            photoUri = "scheme://path",
            date = "2021-09-01",
          )
      )

    reportingVerifier.apply {
      verifyFormSuccessResponse(machineNumber = "123", description = "Broken machine")
    }
  }

  @Test
  fun testFieldsWithEmptyValues() {
    loginRobot.apply {
      enterUsernamePassword("test", "Travelport")
      login()
    }

    coEvery { dashboardViewModel.uiState.value } returns
      DashboardUiState.Success(
        items = DataProvider.fitnessClasses(),
        profile = DataProvider.profile(),
        booking = DataProvider.bookings(),
      )

    reportingRobot.apply {
      tapOnReportNavTab()
      performSend()
    }

    reportingVerifier.apply { verifyEmptyFieldsError() }
  }
}
