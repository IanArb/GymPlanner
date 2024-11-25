package com.ianarbuckle.gymplanner.android.gymlocations.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import kotlinx.collections.immutable.toImmutableList

@Composable
fun GymLocationsScreen(
    gymLocationsViewModel: GymLocationsViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    onNavigateTo: (String) -> Unit
) {

    LaunchedEffect(true) {
        gymLocationsViewModel.fetchGymLocations()
    }

    when (val uiState = gymLocationsViewModel.uiState.collectAsState().value) {
        is GymLocationsUiState.Failure -> {
            RetryErrorScreen(
                text = "Failed to retrieve gym locations."
            ) {
                gymLocationsViewModel.fetchGymLocations()
            }
        }

        is GymLocationsUiState.Success -> {
            GymLocationsSelection(
                innerPadding = contentPadding,
                gyms = uiState.gymLocations.toImmutableList(),
            ) {
                onNavigateTo(it.title)
            }
        }

        is GymLocationsUiState.Loading -> {
            CircularProgressIndicator()
        }
    }
}