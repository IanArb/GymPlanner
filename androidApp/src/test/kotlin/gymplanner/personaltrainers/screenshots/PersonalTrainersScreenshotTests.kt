package gymplanner.personaltrainers.screenshots

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.ianarbuckle.gymplanner.android.personaltrainers.presentation.PersonalTrainerItem
import gymplanner.utils.DataProvider
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
class PersonalTrainersScreenshotTests {

    @get:Rule
    val roborazziRule: RoborazziRule = createRoborazziRule()

    @get:Rule
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity> =
        createComposeTestRule<ComponentActivity>()

    private val testModule = module {
        single<DataStore<Preferences>> { FakeDataStore() }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(listOf(testModule))

    @Test
    fun verify_personal_trainers_card_is_displayed_correctly_in_light_mode() {
        composeTestRule.setContent {
            ScreenTestPreview {
                Surface {
                    PersonalTrainerItem(
                        personalTrainer = DataProvider.personalTrainers().first(),
                        onSocialLinkClick = { },
                        onBookTrainerClick = { },
                        onItemClick = { }
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Config(qualifiers = "night")
    @Test
    fun verify_personal_trainers_card_is_displayed_correctly_in_night_mode() {
        composeTestRule.setContent {
            ScreenTestPreview(isDarkTheme = true) {
                Surface {
                    PersonalTrainerItem(
                        personalTrainer = DataProvider.personalTrainers().first(),
                        onSocialLinkClick = { },
                        onBookTrainerClick = { },
                        onItemClick = { }
                    )
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }
}