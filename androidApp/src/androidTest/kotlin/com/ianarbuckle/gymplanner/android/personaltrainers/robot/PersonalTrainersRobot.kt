package com.ianarbuckle.gymplanner.android.personaltrainers.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ianarbuckle.gymplanner.android.gymlocations.presentation.GymLocationsGridTag
import com.ianarbuckle.gymplanner.android.personaltrainers.presentation.PersonalTrainersItemsTag

class PersonalTrainersRobot(private val composeTestRule: ComposeTestRule) {

  fun clickOnPersonalTrainersNavTab() {
    composeTestRule.onNodeWithText("Personal Trainers").performClick()
  }

  fun clickOnGymLocation(position: Int) {
    composeTestRule.onNodeWithTag(GymLocationsGridTag).onChildAt(position).performClick()
  }

  fun clickOnPersonalTrainerCard(position: Int) {
    composeTestRule.onNodeWithTag(PersonalTrainersItemsTag).onChildAt(position).performClick()
  }
}
