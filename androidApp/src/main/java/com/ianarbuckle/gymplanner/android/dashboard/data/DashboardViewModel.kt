package com.ianarbuckle.gymplanner.android.dashboard.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.GymPlanner
import com.ianarbuckle.gymplanner.android.dashboard.presentation.DashboardUiState
import com.ianarbuckle.gymplanner.android.core.utils.CoroutinesDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val gymPlanner: GymPlanner,
    private val coroutineDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)

    val uiState = _uiState.asStateFlow()

    fun fetchFitnessClasses() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val result = gymPlanner.fetchTodaysFitnessClasses()

            result.onSuccess { classes ->
                _uiState.update {
                    DashboardUiState.FitnessClasses(classes.toImmutableList())
                }
            }

            result.onFailure {
                _uiState.update {
                    DashboardUiState.Failure
                }
            }
        }
    }
}