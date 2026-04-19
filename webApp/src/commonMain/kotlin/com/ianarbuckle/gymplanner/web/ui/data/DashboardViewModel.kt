package com.ianarbuckle.gymplanner.web.ui.data

import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.facilities.FacilitiesRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DashboardViewModel(private val scope: CoroutineScope) : KoinComponent {

    private val facilitiesRepository by inject<FacilitiesRepository>()
    private val _uiState: MutableStateFlow<DashboardUiState> =
        MutableStateFlow(DashboardUiState.Idle)
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun fetchFacilities(gymLocation: GymLocation) {
        scope.launch {
            _uiState.update { DashboardUiState.Loading }
            try {
                val result = facilitiesRepository.getFacilitiesStatus(gymLocation)
                _uiState.update {
                    if (result.isSuccess) {
                        DashboardUiState.Success(
                            result.getOrNull()?.toImmutableList() ?: persistentListOf()
                        )
                    } else {
                        DashboardUiState.Error(
                            result.exceptionOrNull()?.message ?: "Failed to load facilities"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { DashboardUiState.Error(e.message ?: "An unknown error occurred") }
            }
        }
    }
}
