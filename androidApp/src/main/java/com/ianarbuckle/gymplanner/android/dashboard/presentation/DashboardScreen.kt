package com.ianarbuckle.gymplanner.android.dashboard.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun DashboardScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
) {
    LaunchedEffect(true) { dashboardViewModel.fetchFitnessClasses() }

    val uiState = dashboardViewModel.uiState.collectAsState()

    when (uiState.value) {
        is DashboardUiState.Failure -> {
            RetryErrorScreen(
                modifier = modifier,
                text = "Failed to retrieve dashboard.",
                onClick = { dashboardViewModel.fetchFitnessClasses() },
            )
        }

        is DashboardUiState.Success -> {
            DashboardContent(
                modifier = modifier,
                innerPadding = contentPadding,
                classes = (uiState.value as DashboardUiState.Success).items,
                onViewScheduleClick = {},
                onBookPersonalTrainerClick = {},
                bookings = (uiState.value as DashboardUiState.Success).booking,
            )
        }

        is DashboardUiState.Loading -> {
            DashboardLoadingShimmer(
                modifier = modifier,
                shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View),
            )
        }

        DashboardUiState.Idle -> {
            // Do nothing
        }
    }
}
