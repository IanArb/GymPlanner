package gymplanner.chat.screenshots

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.ianarbuckle.gymplanner.android.chat.presentation.ConnectionBanner
import gymplanner.utils.FakeDataStore
import gymplanner.utils.KoinTestRule
import gymplanner.utils.ScreenTestPreview
import gymplanner.utils.createComposeTestRule
import gymplanner.utils.createRoborazziRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ConnectionBannerScreenshotTests {

    @get:Rule val roborazziRule: RoborazziRule = createRoborazziRule()

    @get:Rule
    val composeTestRule:
        AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity> =
        createComposeTestRule<ComponentActivity>()

    private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }

    @get:Rule val koinTestRule = KoinTestRule(listOf(testModule))

    @Test
    fun verify_connection_banner_in_light_mode() {
        composeTestRule.setContent {
            ScreenTestPreview {
                Surface {
                    ConnectionBanner(
                        visible = true,
                        connectionText = "Connecting...",
                        backgroundColor = Color.Black,
                        textColor = Color.White,
                        iconColor = Color.White,
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Config(qualifiers = "+night")
    @Test
    fun verify_connection_banner_in_dark_mode() {
        composeTestRule.setContent {
            ScreenTestPreview(isDarkTheme = true) {
                Surface {
                    ConnectionBanner(
                        visible = true,
                        connectionText = "Connecting...",
                        backgroundColor = Color.White,
                        textColor = Color.Black,
                        iconColor = Color.Black,
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }
}
