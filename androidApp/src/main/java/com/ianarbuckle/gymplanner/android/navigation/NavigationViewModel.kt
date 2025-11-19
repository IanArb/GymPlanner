package com.ianarbuckle.gymplanner.android.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.REMEMBER_ME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NavigationViewModel
@Inject
constructor(private val dataStoreRepository: DataStoreRepository) : ViewModel() {

    private val _isLoggedIn = Channel<Boolean>()
    val isLoggedIn = _isLoggedIn.receiveAsFlow()

    val navigationBackStack = mutableStateListOf<NavKey>(DashboardScreen)

    init {
        viewModelScope.launch {
            _isLoggedIn.send(dataStoreRepository.getBooleanData(key = REMEMBER_ME_KEY) ?: false)
        }
    }

    fun onNavigate(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.NavigateToDashboard -> {
                navigationBackStack.add(DashboardScreen)
            }
            NavigationEvent.NavigateBack -> {
                navigationBackStack.removeLastOrNull()
            }
            is NavigationEvent.NavigateToAvailability -> {
                navigationBackStack.add(
                    AvailabilityScreen(
                        personalTrainerId = event.personalTrainerId,
                        name = event.name,
                        imageUrl = event.imageUrl,
                        gymLocation = event.gymLocation,
                        qualifications = event.qualifications,
                    )
                )
            }
            is NavigationEvent.NavigateToBooking -> {
                navigationBackStack.add(
                    BookingScreen(
                        personalTrainerId = event.personalTrainerId,
                        timeSlotId = event.timeSlotId,
                        selectedDate = event.selectedDate,
                        selectedTimeSlot = event.selectedTimeSlot,
                        personalTrainerName = event.personalTrainerName,
                        personalTrainerAvatarUrl = event.personalTrainerAvatarUrl,
                        location = event.location,
                    )
                )
            }
            is NavigationEvent.NavigateToChat -> {
                navigationBackStack.add(
                    ConversationScreen(username = event.username, userId = event.userId)
                )
            }
            NavigationEvent.NavigateToGymLocations -> {
                navigationBackStack.add(GymLocationsScreen)
            }
            NavigationEvent.NavigateToLogin -> {
                navigationBackStack.add(LoginScreen)
            }
            is NavigationEvent.NavigateToPersonalTrainers ->
                navigationBackStack.add(PersonalTrainersScreen(gymLocation = event.gymLocation))
            NavigationEvent.NavigateToReportMachineBroken -> {
                navigationBackStack.add(ReportMachineBroken)
            }

            is NavigationEvent.NavigateToPersonalTrainersDetails -> {
                navigationBackStack.add(
                    PersonalTrainersDetailScreen(
                        name = event.name,
                        bio = event.bio,
                        imageUrl = event.imageUrl,
                    )
                )
            }

            is NavigationEvent.NavigationBottomBar -> {
                navigationBackStack.add(event.destination)
            }
        }
    }
}
