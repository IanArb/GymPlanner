package com.ianarbuckle.gymplanner.android.gymlocations.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GymLocationsViewModel
@Inject
constructor(private val gymLocationsRepository: GymLocationsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<GymLocationsUiState>(GymLocationsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchGymLocations() {
        viewModelScope.launch {
            gymLocationsRepository
                .fetchGymLocations()
                .fold(
                    onSuccess = { gymLocations ->
                        _uiState.update { GymLocationsUiState.Success(gymLocations) }
                    },
                    onFailure = { _uiState.update { GymLocationsUiState.Failure } },
                )
        }
    }
}
