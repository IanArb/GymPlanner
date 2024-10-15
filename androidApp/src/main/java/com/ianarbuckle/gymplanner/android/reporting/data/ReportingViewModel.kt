package com.ianarbuckle.gymplanner.android.reporting.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.GymPlanner
import com.ianarbuckle.gymplanner.android.core.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.android.reporting.presentation.FormFaultReportUiState
import com.ianarbuckle.gymplanner.model.FaultReport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportingViewModel @Inject constructor(
    private val gymPlanner: GymPlanner,
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider,
): ViewModel() {

    private val _uiState = MutableStateFlow<FormFaultReportUiState>(FormFaultReportUiState.FormIdle)

    val uiState = _uiState.asStateFlow()

    fun submitFault(faultReport: FaultReport) {
        _uiState.update {
            FormFaultReportUiState.FormLoading
        }
        viewModelScope.launch(coroutinesDispatcherProvider.io) {
            val result = gymPlanner.submitFault(faultReport)

            result.onSuccess {
                _uiState.update {
                    FormFaultReportUiState.FormSuccess(faultReport)
                }
            }

            result.onFailure {
                _uiState.update {
                    FormFaultReportUiState.FormError
                }
            }
        }
    }
}