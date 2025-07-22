package com.ianarbuckle.gymplanner.android.reporting.verifier

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText

class ReportingVerifier(private val testComposeRule: ComposeTestRule) {

    fun verifyReportingScreenIsDisplayed() {
        testComposeRule.onNodeWithText("Report Machine").assertExists()
    }

    fun verifyFormSuccessResponse(machineNumber: String, description: String) {
        testComposeRule.onNodeWithText(machineNumber).assertExists()
        testComposeRule.onNodeWithText(description)
        testComposeRule.onNodeWithText("Report again").assertExists()
    }

    fun verifyFormErrorResponse() {
        testComposeRule.onNodeWithText("Failed to send report").assertExists()
    }

    fun verifyEmptyFieldsError() {
        testComposeRule.onNodeWithText("Please provide a photo").assertExists()
        testComposeRule.onNodeWithText("Please provide a machine number").assertExists()
        testComposeRule.onNodeWithText("Please provide a description").assertExists()
    }
}
