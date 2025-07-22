package com.ianarbuckle.gymplanner.android.login.verifier

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText

class LoginVerifier(private val composeTestRule: ComposeTestRule) {

    fun checkErrorText(errorText: String) {
        composeTestRule.onNodeWithText(errorText).assertExists()
    }

    fun checkErrorTextIsNotDisplayed(errorText: String) {
        composeTestRule.onNodeWithText(errorText).assertDoesNotExist()
    }

    fun verifyDashboardScreenIsDisplayed(text: String) {
        composeTestRule.onNodeWithText(text).assertExists()
    }
}
