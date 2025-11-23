package com.ianarbuckle.gymplanner.android.reporting

import android.Manifest
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
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.login.robot.LoginRobot
import com.ianarbuckle.gymplanner.android.reporting.fakes.FakeFaultRepository
import com.ianarbuckle.gymplanner.android.reporting.robot.ReportingRobot
import com.ianarbuckle.gymplanner.android.reporting.verifier.ReportingVerifier
import com.ianarbuckle.gymplanner.android.utils.ComposeIdlingResource
import com.ianarbuckle.gymplanner.android.utils.ConditionalPermissionRule
import com.ianarbuckle.gymplanner.android.utils.FakeDataStore
import com.ianarbuckle.gymplanner.android.utils.KoinTestRule
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
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

    @get:Rule(order = 3)
    val postNotificationsPermissionRule =
        ConditionalPermissionRule(permission = Manifest.permission.POST_NOTIFICATIONS, minSdk = 33)

    private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }

    @get:Rule val koinTestRule = KoinTestRule(modules = listOf(testModule))

    val dashboardViewModel = mockk<DashboardViewModel>(relaxed = true)

    @Inject lateinit var faultRepository: FaultReportingRepository

    val fakeFaultRepository: FakeFaultRepository
        get() = faultRepository as FakeFaultRepository

    private val loginRobot = LoginRobot(composeTestRule)
    private val reportingRobot = ReportingRobot(composeTestRule)
    private val reportingVerifier = ReportingVerifier(composeTestRule)

    val idlingResource = ComposeIdlingResource()

    @Before
    fun setup() {
        hiltTestRule.inject()
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

        reportingRobot.apply {
            tapOnReportNavTab()
            populateForm()
            performPhotoAction()
        }

        reportingRobot.apply { performSend() }

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

        reportingRobot.apply {
            tapOnReportNavTab()
            performSend()
        }

        reportingVerifier.apply { verifyEmptyFieldsError() }
    }

    @Test
    fun testReportingScreenWithFailedReport() {
        fakeFaultRepository.shouldReturnError = true

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

        reportingRobot.apply {
            tapOnReportNavTab()
            populateForm()
            performPhotoAction()
        }

        reportingRobot.apply { performSend() }

        reportingVerifier.apply { verifyFormErrorResponse() }
    }
}
