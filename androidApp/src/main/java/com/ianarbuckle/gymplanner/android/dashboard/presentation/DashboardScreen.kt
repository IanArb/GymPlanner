package com.ianarbuckle.gymplanner.android.dashboard.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen

@Composable
fun DashboardScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by dashboardViewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is DashboardUiState.Failure -> {
            RetryErrorScreen(
                modifier = modifier,
                text = "Failed to retrieve dashboard.",
                onClick = dashboardViewModel::fetchFitnessClasses,
            )
        }

        is DashboardUiState.Success -> {
            DashboardContent(
                modifier = modifier,
                innerPadding = contentPadding,
                classes = state.items,
                onViewScheduleClick = {},
                onBookPersonalTrainerClick = {},
                bookings = state.booking,
            )
        }

        is DashboardUiState.Loading -> {
            DashboardLoadingShimmer(innerPadding = contentPadding, modifier = modifier)
        }

        DashboardUiState.Idle -> {
            // Do nothing
        }
    }
}
