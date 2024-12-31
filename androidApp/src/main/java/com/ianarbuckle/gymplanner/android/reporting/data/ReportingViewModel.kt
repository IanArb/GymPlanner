package com.ianarbuckle.gymplanner.android.reporting.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportingViewModel @Inject constructor(
    private val faultReportingRepository: FaultReportingRepository,
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FormFaultReportUiState>(FormFaultReportUiState.FormIdle)

    val uiState = _uiState.asStateFlow()

    fun submitFault(faultReport: FaultReport) {
        _uiState.update {
            FormFaultReportUiState.FormLoading
        }
        viewModelScope.launch(coroutinesDispatcherProvider.io) {
            val result = faultReportingRepository.saveFaultReport(faultReport)

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
