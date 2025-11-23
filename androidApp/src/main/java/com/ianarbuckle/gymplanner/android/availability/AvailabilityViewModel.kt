package com.ianarbuckle.gymplanner.android.availability

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.android.utils.calendarMonth
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalTime::class)
@HiltViewModel(assistedFactory = AvailabilityViewModel.Factory::class)
class AvailabilityViewModel
@AssistedInject
constructor(
    private val availabilityRepository: AvailabilityRepository,
    @Assisted private val personalTrainerId: String,
    private val clock: Clock,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(personalTrainerId: String): AvailabilityViewModel
    }

    private val _availabilityUiState =
        MutableStateFlow<AvailabilityUiState>(AvailabilityUiState.Idle)
    val availabilityUiState = _availabilityUiState.asStateFlow()

    init {
        fetchAvailability()
    }

    @OptIn(ExperimentalTime::class)
    fun fetchAvailability() {
        viewModelScope.launch {
            val currentDateTime =
                clock.now().toLocalDateTime(TimeZone.Companion.currentSystemDefault())

            _availabilityUiState.update { AvailabilityUiState.Loading }

            val checkAvailabilityDeferred = async {
                availabilityRepository.checkAvailability(
                    personalTrainerId = personalTrainerId,
                    month = currentDateTime.calendarMonth(),
                )
            }

            val fetchAvailabilityDeferred = async {
                availabilityRepository.getAvailability(
                    personalTrainerId = personalTrainerId,
                    month = currentDateTime.calendarMonth(),
                )
            }

            supervisorScope {
                val checkAvailabilityResponse = checkAvailabilityDeferred.await()
                val fetchAvailabilityResponse = fetchAvailabilityDeferred.await()

                launch {
                    val state =
                        fetchAvailabilityResponse.fold(
                            onSuccess = { availability ->
                                checkAvailabilityResponse.fold(
                                    onSuccess = { checkAvailability ->
                                        AvailabilityUiState.AvailabilitySuccess(
                                            availability = availability,
                                            isPersonalTrainerAvailable =
                                                checkAvailability.isAvailable,
                                        )
                                    },
                                    onFailure = { AvailabilityUiState.Failed },
                                )
                            },
                            onFailure = { AvailabilityUiState.Failed },
                        )

                    _availabilityUiState.update { state }
                }
            }
        }
    }
}
