package gymplanner.utils

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule

inline fun <reified A : ComponentActivity> createComposeTestRule():
  AndroidComposeTestRule<ActivityScenarioRule<A>, A> {
  return createAndroidComposeRule<A>()
}
