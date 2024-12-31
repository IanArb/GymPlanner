package com.ianarbuckle.gymplanner.android.gymlocations.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

class GymLocationsRobot(private val composeTestRule: ComposeTestRule) {

    fun tapOnGymLocationsNavTab() {
        composeTestRule.onNodeWithText("Personal Trainers")
            .performClick()
    }
}
