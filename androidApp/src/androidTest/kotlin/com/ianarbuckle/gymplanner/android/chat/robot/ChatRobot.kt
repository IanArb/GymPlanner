package com.ianarbuckle.gymplanner.android.chat.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class ChatRobot(private val composeTestRule: ComposeTestRule) {

  fun sendMessage(message: String) {
    composeTestRule.onNodeWithText("Type a message").performTextInput(message)
    composeTestRule.onNodeWithTag("SendButton").performClick()
  }
}
