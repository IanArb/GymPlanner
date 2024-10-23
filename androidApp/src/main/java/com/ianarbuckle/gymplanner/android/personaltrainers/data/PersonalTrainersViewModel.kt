package com.ianarbuckle.gymplanner.android.personaltrainers.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalTrainersViewModel @Inject constructor(
    private val gymPlanner: GymPlanner,
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel() {

    private val _uiState: MutableStateFlow<PersonalTrainersState> = MutableStateFlow(PersonalTrainersState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchPersonalTrainers(gymLocation: GymLocation) {
        viewModelScope.launch(coroutinesDispatcherProvider.io) {
            gymPlanner.fetchPersonalTrainers(gymLocation).fold(
                onSuccess = { trainers ->
                    _uiState.update {
                        PersonalTrainersState.Success(trainers)
                    }
                },
                onFailure = {
                    _uiState.update {
                        PersonalTrainersState.Failure
                    }
                }
            )
        }
    }
}