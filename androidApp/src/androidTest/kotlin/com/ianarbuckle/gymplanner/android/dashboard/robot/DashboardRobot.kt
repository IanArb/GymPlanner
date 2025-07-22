package com.ianarbuckle.gymplanner.android.dashboard.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

class DashboardRobot(private val composeTestRule: ComposeTestRule) {

    fun clickOnChat() {
        composeTestRule.onNodeWithTag("Chat").performClick()
    }
}
