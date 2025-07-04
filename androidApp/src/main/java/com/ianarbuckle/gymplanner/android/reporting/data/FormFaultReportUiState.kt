package com.ianarbuckle.gymplanner.android.reporting.data

import androidx.compose.runtime.Stable
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport

sealed interface FormFaultReportUiState {

  data object FormLoading : FormFaultReportUiState

  data class FormSuccess(@Stable val data: FaultReport) : FormFaultReportUiState

  data object FormError : FormFaultReportUiState

  data object FormIdle : FormFaultReportUiState
}
