package com.ianarbuckle.gymplanner.android.personaltrainers.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PersonalTrainersViewModel
@Inject
constructor(private val personalTrainersRepository: PersonalTrainersRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<PersonalTrainersUiState> =
        MutableStateFlow(PersonalTrainersUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchPersonalTrainers(gymLocation: GymLocation) {
        viewModelScope.launch {
            personalTrainersRepository
                .fetchPersonalTrainers(gymLocation)
                .fold(
                    onSuccess = { trainers ->
                        _uiState.update { PersonalTrainersUiState.Success(trainers) }
                    },
                    onFailure = { _uiState.update { PersonalTrainersUiState.Failure } },
                )
        }
    }
}
