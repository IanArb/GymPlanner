package com.ianarbuckle.gymplanner.android.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.GymPlanner
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for the client's workout screen
 * @param gymPlanner a [GymPlanner] that provides APIs for the screen
 * @param coroutineDispatcherProvider provides dispatchers for coroutines
 */
@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val gymPlanner: GymPlanner,
    private val coroutineDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClientWorkoutUiState>(ClientWorkoutUiState.Idle)

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            gymPlanner.fetchAllClients()
                .distinctUntilChanged()
                .collect { client ->
                    _uiState.value = ClientWorkoutUiState.ClientClientWorkout(client)
                }

        }
    }

}