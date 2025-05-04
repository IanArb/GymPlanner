package com.ianarbuckle.gymplanner.android.availability.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ianarbuckle.gymplanner.android.gymlocations.presentation.GymLocationsGridTag

class AvailabilityRobot(private val composeTestRule: ComposeTestRule) {

    fun clickOnBookPersonalTrainer() {
        composeTestRule.onAllNodesWithText("Book now")[0].performClick()
    }

    fun clickOnGymLocation(position: Int) {
        composeTestRule.onNodeWithTag(GymLocationsGridTag)
            .onChildAt(position)
            .performClick()
    }

    fun clickOnPersonalTrainersNavTab() {
        composeTestRule.onNodeWithText("Personal Trainers").performClick()
    }

    fun clickOnTimeSlotDay(position: Int) {
        composeTestRule.onNodeWithTag("CalendarGridTestTag")
            .onChildAt(position)
            .performClick()
    }

    fun clickOnTimeSlot(position: Int) {
        composeTestRule.onNodeWithTag("AvailableTimesGrid")
            .onChildAt(position)
            .performClick()
    }

    fun clickOnBookAppointment() {
        composeTestRule.onNodeWithText("Book appointment").performClick()
    }
}
