package com.ianarbuckle.gymplanner.android.navigation

import androidx.navigation3.runtime.NavKey
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.serialization.Serializable

@Serializable object Root : NavKey

@Serializable object DashboardScreen : NavKey

@Serializable object ReportMachineBroken : NavKey

@Serializable object GymLocationsScreen : NavKey

@Serializable data class PersonalTrainersScreen(val gymLocation: GymLocation) : NavKey

@Serializable
data class PersonalTrainersDetailScreen(val name: String, val bio: String, val imageUrl: String) :
    NavKey

@Serializable object LoginScreen : NavKey

@Serializable
data class AvailabilityScreen(
    val personalTrainerId: String,
    val name: String,
    val imageUrl: String,
    val gymLocation: String,
    val qualifications: List<String>,
) : NavKey

@Serializable
data class BookingScreen(
    val personalTrainerId: String,
    val timeSlotId: String,
    val selectedDate: String,
    val selectedTimeSlot: String,
    val personalTrainerName: String,
    val personalTrainerAvatarUrl: String,
    val location: String,
) : NavKey

@Serializable data class ConversationScreen(val username: String, val userId: String) : NavKey
