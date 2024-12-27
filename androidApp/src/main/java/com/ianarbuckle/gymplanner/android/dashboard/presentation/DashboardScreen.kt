package com.ianarbuckle.gymplanner.android.dashboard.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen

@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
) {
    LaunchedEffect(true) {
        dashboardViewModel.fetchFitnessClasses()
    }

    val uiState = dashboardViewModel.uiState.collectAsState()

    when (uiState.value) {
        is DashboardUiState.Failure -> {
            RetryErrorScreen(
                text = "Failed to retrieve dashboard."
            ) {
                dashboardViewModel.fetchFitnessClasses()
            }
        }

        is DashboardUiState.Success -> {
            DashboardContent(
                innerPadding = contentPadding,
                items = (uiState.value as DashboardUiState.Success).items,
                onViewScheduleClick = {

                },
                onBookPersonalTrainerClick = {

                }
            )
        }

        is DashboardUiState.Loading -> {
            CircularProgressIndicator()
        }

        DashboardUiState.Idle -> {
            // Do nothing
        }
    }
}