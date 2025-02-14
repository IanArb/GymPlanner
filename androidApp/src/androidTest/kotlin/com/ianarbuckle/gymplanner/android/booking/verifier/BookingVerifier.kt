package com.ianarbuckle.gymplanner.android.booking.verifier

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.ianarbuckle.gymplanner.android.booking.presentation.bookingscreen.AvailableTimesGrid
import com.ianarbuckle.gymplanner.android.booking.presentation.bookingscreen.CalendarGridTestTag

class BookingVerifier(private val composeTestRule: ComposeTestRule) {

    fun verifyPersonalTrainerNameAndDescription(
        name: String,
        qualifications: List<String>,
    ) {
        val items = qualifications.joinToString(", ")
        composeTestRule.onNodeWithText(name)
            .assertExists()
        composeTestRule.onNodeWithText(items)
    }

    fun verifyAvailableTimesSize(size: Int) {
        composeTestRule.onNodeWithTag(AvailableTimesGrid)
            .assertExists()
            .onChildren()
            .assertCountEquals(size)
    }

    fun verifyCalendarGridSize(size: Int) {
        composeTestRule.onNodeWithTag(CalendarGridTestTag)
            .assertExists()
            .onChildren()
            .assertCountEquals(size)
    }

    fun verifyCalendarMonth(month: String) {
        composeTestRule.onNodeWithText(month)
            .assertExists()
    }

    fun verifyErrorState() {
        composeTestRule.onNodeWithText("Failed to load availability.")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap to retry")
            .assertIsDisplayed()
    }

    fun verifyConfirmBookingButton() {
        composeTestRule.onNodeWithText("Confirm Booking")
            .assertIsDisplayed()
    }
}
