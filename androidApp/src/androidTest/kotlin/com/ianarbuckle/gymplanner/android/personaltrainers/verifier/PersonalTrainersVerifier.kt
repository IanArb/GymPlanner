package com.ianarbuckle.gymplanner.android.personaltrainers.verifier

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.ianarbuckle.gymplanner.android.personaltrainers.presentation.PersonalTrainersItemsTag

class PersonalTrainersVerifier(private val composeTestRule: ComposeTestRule) {

    fun verifyPersonalTrainersScreenIsDisplayed() {
        composeTestRule.onNodeWithText("Personal Trainers").assertExists()
    }

    fun verifyPersonalTrainersCount(size: Int) {
        composeTestRule.onNodeWithTag(PersonalTrainersItemsTag).onChildren().assertCountEquals(size)
    }

    fun verifyPersonalTrainerCard(position: Int) {
        composeTestRule
            .onNodeWithTag("PersonalTrainersItemsTag")
            .onChildAt(position)
            .assertHasClickAction()
    }

    fun verifyErrorState() {
        composeTestRule.onNodeWithText("Failed to retrieve personal trainers.").assertExists()
        composeTestRule.onNodeWithText("Tap to retry").assertExists()
    }

    fun verifyPersonalTrainerDetail(name: String, description: String) {
        composeTestRule.onNodeWithText(name).assertExists()
        composeTestRule.onNodeWithText(description).assertExists()
        composeTestRule.onNodeWithText("Book now")
    }
}
