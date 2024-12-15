package com.ianarbuckle.gymplanner.android.dashboard.data

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@Stable
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val gymPlanner: GymPlanner,
    private val coroutineDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)

    val uiState = _uiState.asStateFlow()

    fun fetchFitnessClasses() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val userId = gymPlanner.fetchUserId()
            val profileDeferred = async { gymPlanner.fetchProfile(userId) }
            val classesDeferred = async { gymPlanner.fetchTodaysFitnessClasses() }

            val profileResult = profileDeferred.await()
            val classesResult = classesDeferred.await()

            supervisorScope {
                val newState = profileResult.fold(
                    onSuccess = { profile ->
                        classesResult.fold(
                            onSuccess = { classes ->
                                DashboardUiState.Success(
                                    items = classes.toImmutableList(),
                                    profile = profile
                                )
                            },
                            onFailure = { DashboardUiState.Failure }
                        )
                    },
                    onFailure = { DashboardUiState.Failure }
                )

                _uiState.update { newState }
            }
        }
    }
}