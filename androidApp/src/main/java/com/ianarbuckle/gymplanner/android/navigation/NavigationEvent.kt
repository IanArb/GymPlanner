package com.ianarbuckle.gymplanner.android.navigation

import androidx.navigation3.runtime.NavKey
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

sealed interface NavigationEvent {
    object NavigateToDashboard : NavigationEvent

    object NavigateToReportMachineBroken : NavigationEvent

    object NavigateToGymLocations : NavigationEvent

    data class NavigateToPersonalTrainers(val gymLocation: GymLocation) : NavigationEvent

    data class NavigateToPersonalTrainersDetails(
        val name: String,
        val bio: String,
        val imageUrl: String,
    ) : NavigationEvent

    data class NavigateToAvailability(
        val personalTrainerId: String,
        val name: String,
        val imageUrl: String,
        val gymLocation: String,
        val qualifications: List<String>,
    ) : NavigationEvent

    data class NavigateToBooking(
        val personalTrainerId: String,
        val timeSlotId: String,
        val selectedDate: String,
        val selectedTimeSlot: String,
        val personalTrainerName: String,
        val personalTrainerAvatarUrl: String,
        val location: String,
    ) : NavigationEvent

    data class NavigateToChat(val username: String, val userId: String) : NavigationEvent

    object NavigateBack : NavigationEvent

    object NavigateToLogin : NavigationEvent

    data class NavigationBottomBar(val destination: NavKey) : NavigationEvent
}
