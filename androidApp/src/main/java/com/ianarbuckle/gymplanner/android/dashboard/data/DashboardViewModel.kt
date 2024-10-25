package com.ianarbuckle.gymplanner.android.dashboard.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
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
           gymPlanner.fetchTodaysFitnessClasses().fold(
                onSuccess = { classes ->
                    _uiState.update {
                        DashboardUiState.FitnessClasses(classes.toImmutableList())
                    }
                },
                onFailure = {
                    _uiState.update {
                        DashboardUiState.Failure
                    }
                }
            )
        }
    }
}