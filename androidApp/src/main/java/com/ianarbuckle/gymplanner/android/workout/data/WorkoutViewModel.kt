package com.ianarbuckle.gymplanner.android.workout.data

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.clients.ClientsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for the client's workout screen
 * @param gymPlanner a [GymPlanner] that provides APIs for the screen
 * @param coroutineDispatcherProvider provides dispatchers for coroutines
 */
@HiltViewModel
class WorkoutViewModel @Inject constructor(
    @Stable
    private val clientsRepository: ClientsRepository,
    private val coroutineDispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClientWorkoutUiState>(ClientWorkoutUiState.Loading)

    val uiState = _uiState.asStateFlow()

    fun fetchClients() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val result = clientsRepository.fetchClients()

            result.onSuccess { clients ->
                _uiState.update {
                    ClientWorkoutUiState.ClientClientWorkout(clients)
                }
            }

            result.onFailure {
                _uiState.update {
                    ClientWorkoutUiState.Failure
                }
            }
        }
    }
}
