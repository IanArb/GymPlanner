package com.ianarbuckle.gymplanner.android.availability

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.android.utils.calendarMonth
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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

@HiltViewModel
class AvailabilityViewModel
@OptIn(ExperimentalTime::class)
@Inject
constructor(
    private val availabilityRepository: AvailabilityRepository,
    val savedStateHandle: SavedStateHandle,
    private val clock: Clock,
) : ViewModel() {

    private val _availabilityUiState =
        MutableStateFlow<AvailabilityUiState>(AvailabilityUiState.Idle)
    val availabilityUiState = _availabilityUiState.asStateFlow()

    private val personalTrainerId = savedStateHandle.get<String>("personalTrainerId")

    @OptIn(ExperimentalTime::class)
    fun fetchAvailability() {
        viewModelScope.launch {
            val currentDateTime =
                clock.now().toLocalDateTime(TimeZone.Companion.currentSystemDefault())

            _availabilityUiState.update { AvailabilityUiState.Loading }

            if (!personalTrainerId.isNullOrEmpty()) {
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
            } else {
                _availabilityUiState.update { AvailabilityUiState.Failed }
            }
        }
    }
}
