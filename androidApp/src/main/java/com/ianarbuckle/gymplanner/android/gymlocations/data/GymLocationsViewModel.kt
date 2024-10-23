package com.ianarbuckle.gymplanner.android.gymlocations.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GymLocationsViewModel @Inject constructor(
    private val gymPlanner: GymPlanner,
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GymLocationsUiState>(GymLocationsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchGymLocations() {
        viewModelScope.launch(coroutinesDispatcherProvider.io) {
            gymPlanner.fetchGymLocations().fold(
                onSuccess = { gymLocations ->
                    _uiState.update {
                        GymLocationsUiState.Success(gymLocations)
                    }
                },
                onFailure = {
                    _uiState.update {
                        GymLocationsUiState.Failure
                    }
                }
            )
        }
    }
}