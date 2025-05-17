package com.ianarbuckle.gymplanner.android.dashboard.verifier

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText

class DashboardVerifier(private val testComposeRule: ComposeTestRule) {

    fun verifyBookPersonalTrainerTextExists() {
        testComposeRule.onNodeWithText("Book a personal trainer")
            .assertIsDisplayed()
        testComposeRule.onNodeWithText("Book a six week personal trainer program today")
    }

    fun verifyUserBookings() {
        testComposeRule.onNodeWithText("Your Bookings")
            .assertIsDisplayed()
        testComposeRule.onNodeWithText("John Doe")
            .assertIsDisplayed()
    }

    fun verifyFitnessClassesTextExists() {
        testComposeRule.onNodeWithText("Today's Classes")
            .assertIsDisplayed()
        testComposeRule.onNodeWithText("View Weekly Schedule")
            .assertIsDisplayed()
    }

    fun verifyErrorState() {
        testComposeRule.onNodeWithText("Failed to retrieve dashboard.")
            .assertIsDisplayed()
        testComposeRule.onNodeWithText("Tap to retry")
            .assertIsDisplayed()
    }

    fun verifyIconResource(contentDescription: String) {
        testComposeRule.onNodeWithContentDescription(contentDescription)
            .assertIsDisplayed()
    }
}
