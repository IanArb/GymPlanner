package com.ianarbuckle.gymplanner.android.dashboard.data

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.profile.ProfileRepository
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@Stable
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val fitnessClassRepository: FitnessClassRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val coroutineDispatcherProvider: CoroutinesDispatcherProvider,
    private val clock: Clock,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)

    val uiState = _uiState.asStateFlow()

    fun fetchFitnessClasses() {
        _uiState.update { DashboardUiState.Loading }

        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val userId = dataStoreRepository.getStringData(USER_ID) ?: ""
            val profileDeferred = async { profileRepository.fetchProfile(userId) }
            val classesDeferred = async { fetchTodaysFitnessClasses() }

            val profileResult = profileDeferred.await()
            val classesResult = classesDeferred.await()

            supervisorScope {
                val newState = profileResult.fold(
                    onSuccess = { profile ->
                        classesResult.fold(
                            onSuccess = { classes ->
                                DashboardUiState.Success(
                                    items = classes.toImmutableList(),
                                    profile = profile,
                                )
                            },
                            onFailure = { DashboardUiState.Failure },
                        )
                    },
                    onFailure = { DashboardUiState.Failure },
                )

                _uiState.update { newState }
            }
        }
    }

    @Suppress("ReturnCount")
    private suspend fun fetchTodaysFitnessClasses(): Result<ImmutableList<FitnessClass>> {
        val datetimeInSystemZone: LocalDateTime = clock.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val dayOfWeek = datetimeInSystemZone.dayOfWeek

        when (dayOfWeek) {
            DayOfWeek.MONDAY -> {
                return fitnessClassRepository.fetchFitnessClasses(DayOfWeek.MONDAY.name)
            }
            DayOfWeek.TUESDAY -> {
                return fitnessClassRepository.fetchFitnessClasses(DayOfWeek.TUESDAY.name)
            }
            DayOfWeek.WEDNESDAY -> {
                return fitnessClassRepository.fetchFitnessClasses(DayOfWeek.WEDNESDAY.name)
            }
            DayOfWeek.THURSDAY -> {
                return fitnessClassRepository.fetchFitnessClasses(DayOfWeek.THURSDAY.name)
            }
            DayOfWeek.FRIDAY -> {
                return fitnessClassRepository.fetchFitnessClasses(DayOfWeek.FRIDAY.name)
            }
            DayOfWeek.SATURDAY -> {
                return fitnessClassRepository.fetchFitnessClasses(DayOfWeek.SATURDAY.name)
            }
            DayOfWeek.SUNDAY -> {
                return fitnessClassRepository.fetchFitnessClasses(DayOfWeek.SUNDAY.name)
            }
            else -> {
                return Result.failure(exception = Exception("No classes found"))
            }
        }
    }
}
