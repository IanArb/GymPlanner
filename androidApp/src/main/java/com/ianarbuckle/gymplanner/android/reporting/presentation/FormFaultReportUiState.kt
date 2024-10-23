package com.ianarbuckle.gymplanner.android.reporting.presentation

import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport


sealed interface FormFaultReportUiState {

    data object FormLoading : FormFaultReportUiState

    data class FormSuccess(val data: FaultReport) : FormFaultReportUiState

    data object FormError : FormFaultReportUiState

    data object FormIdle : FormFaultReportUiState
}