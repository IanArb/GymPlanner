package com.ianarbuckle.gymplanner.android.booking.verifier

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText

class BookingVerifier(private val composeTestRule: ComposeTestRule) {

  fun verifyBookingIsSuccessful() {
    composeTestRule.onNodeWithText("Session Confirmed!").assertIsDisplayed()
    composeTestRule.onNodeWithText("Go to Home").assertIsDisplayed()
  }

  fun verifyDashboardScreen() {
    composeTestRule.onNodeWithText("Dashboard").assertIsDisplayed()
    composeTestRule.onNodeWithText("Book now").assertIsDisplayed()
    composeTestRule.onNodeWithText("Personal Trainers").assertIsDisplayed()
  }

  fun verifyBookingFailed() {
    composeTestRule.onNodeWithText("Booking Failed").assertIsDisplayed()
    composeTestRule
      .onNodeWithText(
        "We couldn’t confirm your session. Please check your internet connection and try again."
      )
      .assertIsDisplayed()
    composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
  }
}
