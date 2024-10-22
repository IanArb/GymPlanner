package com.ianarbuckle.gymplanner.android.core.navigation

import com.ianarbuckle.gymplanner.model.GymLocation
import kotlinx.serialization.Serializable

@Serializable
object DashboardScreen

@Serializable
object ReportMachineBroken

@Serializable
object GymLocationsScreen

@Serializable
data class PersonalTrainersScreen(
    val gymLocation: GymLocation,
)