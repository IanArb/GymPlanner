package com.ianarbuckle.gymplanner.android.login.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput

class LoginRobot(private val composeTestRule: ComposeTestRule) {

    fun enterUsernamePassword(username: String, password: String) {
        composeTestRule.onNodeWithText("Username").performTextInput(username)
        composeTestRule.onNodeWithText("Username").performImeAction()
        composeTestRule.onNodeWithText("Password").performTextInput(password)
        composeTestRule.onNodeWithText("Password").performImeAction()
    }

    fun login() {
        composeTestRule.onNodeWithText("Login").performClick()
    }
}
