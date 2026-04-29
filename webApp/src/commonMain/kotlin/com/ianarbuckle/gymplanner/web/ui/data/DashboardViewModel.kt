package com.ianarbuckle.gymplanner.web.ui.data

import com.ianarbuckle.gymplanner.common.AvailabilityStatus
import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.common.PersonalTrainer
import com.ianarbuckle.gymplanner.facilities.FacilitiesRepository
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import com.ianarbuckle.gymplanner.web.ui.trainers.TrainerAvailability
import com.ianarbuckle.gymplanner.web.ui.trainers.TrainerItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DashboardViewModel(
    private val scope: CoroutineScope,
    private val imageProxyBase: String = "",
) : KoinComponent {

    private val facilitiesRepository by inject<FacilitiesRepository>()
    private val personalTrainersRepository by inject<PersonalTrainersRepository>()

    private val _uiState: MutableStateFlow<DashboardUiState> =
        MutableStateFlow(DashboardUiState.Idle)
    val uiState: StateFlow<DashboardUiState> = _uiState

    private val _trainersUiState: MutableStateFlow<TrainersUiState> =
        MutableStateFlow(TrainersUiState.Idle)
    val trainersUiState: StateFlow<TrainersUiState> = _trainersUiState

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

    fun fetchTrainerSchedules(date: String, gymLocation: GymLocation) {
        scope.launch {
            _trainersUiState.update { TrainersUiState.Loading }
            try {
                val result = personalTrainersRepository.fetchTrainerSchedules(date, gymLocation)
                _trainersUiState.update {
                    if (result.isSuccess) {
                        TrainersUiState.Success(
                            result
                                .getOrNull()
                                ?.map { it.toTrainerItem(imageProxyBase) }
                                ?.sortedByDescending {
                                    it.availability == TrainerAvailability.AVAILABLE
                                }
                                ?.take(5)
                                ?.toImmutableList() ?: persistentListOf()
                        )
                    } else {
                        TrainersUiState.Error(
                            result.exceptionOrNull()?.message ?: "Failed to load trainers"
                        )
                    }
                }
            } catch (e: Exception) {
                _trainersUiState.update {
                    TrainersUiState.Error(e.message ?: "An unknown error occurred")
                }
            }
        }
    }
}

private fun PersonalTrainer.toTrainerItem(imageProxyBase: String): TrainerItem =
    TrainerItem(
        name = "$firstName $lastName",
        availability =
            when (availabilityStatus) {
                AvailabilityStatus.AVAILABLE -> TrainerAvailability.AVAILABLE
                else -> TrainerAvailability.IN_SESSION
            },
        imageUrl =
            imageUrl
                .takeIf { it.isNotBlank() }
                ?.let { url ->
                    if (imageProxyBase.isNotEmpty()) {
                        url.replace("https://westwood.ie", imageProxyBase)
                    } else {
                        url
                    }
                },
    )
