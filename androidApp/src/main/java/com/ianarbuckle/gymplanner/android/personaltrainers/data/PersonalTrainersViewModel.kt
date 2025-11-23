package com.ianarbuckle.gymplanner.android.personaltrainers.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PersonalTrainersViewModel.Factory::class)
class PersonalTrainersViewModel
@AssistedInject
constructor(
    private val personalTrainersRepository: PersonalTrainersRepository,
    @Assisted private val gymLocation: GymLocation,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(gymLocation: GymLocation): PersonalTrainersViewModel
    }

    private val _uiState: MutableStateFlow<PersonalTrainersUiState> =
        MutableStateFlow(PersonalTrainersUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchPersonalTrainers()
    }

    fun fetchPersonalTrainers() {
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
