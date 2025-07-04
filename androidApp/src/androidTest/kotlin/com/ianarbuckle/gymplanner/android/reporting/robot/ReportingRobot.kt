package com.ianarbuckle.gymplanner.android.reporting.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import com.ianarbuckle.gymplanner.android.reporting.presentation.ImageSelectionTestTag

class ReportingRobot(private val composeTestRule: ComposeTestRule) {

  fun tapOnReportNavTab() {
    composeTestRule.onNodeWithText("Report Machine").performClick()
  }

  fun populateForm() {
    composeTestRule.onNodeWithText("The machine number").performTextInput("123")

    composeTestRule.onNodeWithText("The machine number").performImeAction()

    composeTestRule
      .onNodeWithText("Describe the fault of the machine")
      .performTextInput("Broken machine")

    composeTestRule.onNodeWithText("Describe the fault of the machine").performImeAction()
  }

  fun populateFormWithEmptyFields() {
    composeTestRule.onNodeWithText("Machine number").performTextInput("")

    composeTestRule.onNodeWithText("Description").performTextInput("")

    composeTestRule.onNodeWithText("Send").performClick()
  }

  fun performPhotoAction() {
    composeTestRule.onNodeWithTag(ImageSelectionTestTag).performClick()
  }

  fun performSend() {
    composeTestRule.onNodeWithText("Send").performClick()
  }
}
