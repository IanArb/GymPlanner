package com.ianarbuckle.gymplanner.android.gymlocations.verifier

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.ianarbuckle.gymplanner.android.gymlocations.presentation.GymLocationsGridTag

class GymLocationsVerifier(private val composeTestRule: ComposeTestRule) {

    fun verifyGymLocationsScreenIsDisplayed() {
        composeTestRule.onNodeWithText("Gym Locations")
            .assertIsDisplayed()
    }

    fun verifyGymLocationsItemsSize(size: Int) {
        composeTestRule.onNodeWithTag(GymLocationsGridTag)
            .onChildren()
            .assertCountEquals(size)
    }

    fun verifyErrorStateIsDisplayed() {
        composeTestRule.onNodeWithText("Failed to retrieve gym locations.")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap to retry")
            .assertIsDisplayed()
    }
}
