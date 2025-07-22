package com.ianarbuckle.gymplanner.android.chat.verifier

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText

class ChatVerifier(private val composeTestRule: ComposeTestRule) {

    fun verifyChatScreenIsDisplayed() {
        composeTestRule.onNodeWithTag("Chat").assertExists("Chat screen should be displayed")
    }

    fun verifyMessage(message: String) {
        composeTestRule.onNodeWithText(message).isDisplayed()
    }

    fun verifyComposerIsDisplayed() {
        composeTestRule.onNodeWithText("Type a message").isDisplayed()
        composeTestRule.onNodeWithTag("SendButton").isDisplayed()
    }

    fun verifyErrorState() {
        composeTestRule.onNodeWithText("Failed to load messages.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap to retry").assertIsDisplayed()
    }
}
