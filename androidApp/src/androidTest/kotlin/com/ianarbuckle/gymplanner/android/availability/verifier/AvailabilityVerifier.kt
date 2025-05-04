package com.ianarbuckle.gymplanner.android.availability.verifier

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.ianarbuckle.gymplanner.android.availability.presentation.AvailableTimesGrid
import com.ianarbuckle.gymplanner.android.availability.presentation.CalendarGridTestTag

class AvailabilityVerifier(private val composeTestRule: ComposeTestRule) {

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

    fun verifyBookAppointmentButton() {
        composeTestRule.onNodeWithText("Book appointment")
            .assertIsDisplayed()
    }

    fun verifyConfirmAppointmentButton() {
        composeTestRule.onNodeWithText("Confirm Booking")
            .assertIsDisplayed()
    }

    fun verifyBookingDetails(
        location: String,
        bookingDate: String,
        bookingTime: String,
    ) {
        composeTestRule.onNodeWithText("Location: $location")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Date: $bookingDate")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Time: $bookingTime")
            .assertIsDisplayed()
    }
}
