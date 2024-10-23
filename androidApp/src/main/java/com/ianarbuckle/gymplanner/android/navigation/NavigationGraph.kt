package com.ianarbuckle.gymplanner.android.navigation

import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
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