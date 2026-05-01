package com.ianarbuckle.gymplanner.web.ui.data

import com.ianarbuckle.gymplanner.common.AvailabilityStatus
import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.common.PersonalTrainer
import com.ianarbuckle.gymplanner.facilities.FacilitiesRepository
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import com.ianarbuckle.gymplanner.web.ui.classes.FitnessClassItem
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
    private val ddgImageProxyBase: String = "",
) : KoinComponent {

    private val facilitiesRepository by inject<FacilitiesRepository>()
    private val personalTrainersRepository by inject<PersonalTrainersRepository>()
    private val fitnessClassRepository by inject<FitnessClassRepository>()

    private val _uiState: MutableStateFlow<DashboardUiState> =
        MutableStateFlow(DashboardUiState.Idle)
    val uiState: StateFlow<DashboardUiState> = _uiState

    private val _trainersUiState: MutableStateFlow<TrainersUiState> =
        MutableStateFlow(TrainersUiState.Idle)
    val trainersUiState: StateFlow<TrainersUiState> = _trainersUiState

    private val _classesUiState: MutableStateFlow<ClassesUiState> =
        MutableStateFlow(ClassesUiState.Idle)
    val classesUiState: StateFlow<ClassesUiState> = _classesUiState

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

    fun fetchUpcomingClasses(dayOfWeek: String) {
        scope.launch {
            _classesUiState.update { ClassesUiState.Loading }
            try {
                val result = fitnessClassRepository.fetchFitnessClasses(dayOfWeek)
                _classesUiState.update {
                    if (result.isSuccess) {
                        ClassesUiState.Success(
                            result
                                .getOrNull()
                                ?.map { it.toFitnessClassItem(ddgImageProxyBase) }
                                ?.toImmutableList() ?: persistentListOf()
                        )
                    } else {
                        ClassesUiState.Error(
                            result.exceptionOrNull()?.message ?: "Failed to load classes"
                        )
                    }
                }
            } catch (e: Exception) {
                _classesUiState.update {
                    ClassesUiState.Error(e.message ?: "An unknown error occurred")
                }
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

private fun FitnessClass.toFitnessClassItem(ddgImageProxyBase: String): FitnessClassItem =
    FitnessClassItem(
        name = name,
        description = description,
        timeSlot = "${startTime.toAmPm()} - ${endTime.toAmPm()}",
        imageUrl =
            imageUrl
                .takeIf { it.isNotBlank() }
                ?.let { url ->
                    if (ddgImageProxyBase.isNotEmpty()) {
                        url.replace("https://external-content.duckduckgo.com", ddgImageProxyBase)
                    } else {
                        url
                    }
                } ?: "",
    )

private fun String.toAmPm(): String {
    val parts = split(":")
    if (parts.size < 2) return this
    val hour = parts[0].toIntOrNull() ?: return this
    val minute = parts[1].take(2)
    val period = if (hour < 12) "AM" else "PM"
    val displayHour =
        when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
    return "$displayHour:$minute $period"
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
