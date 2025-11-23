package com.ianarbuckle.gymplanner.android.gymlocations.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.collections.immutable.toImmutableList

@Composable
fun GymLocationsScreen(
    contentPadding: PaddingValues,
    onNavigateTo: (GymLocation) -> Unit,
    modifier: Modifier = Modifier,
    gymLocationsViewModel: GymLocationsViewModel = hiltViewModel(),
) {
    val uiState by gymLocationsViewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is GymLocationsUiState.Failure -> {
            RetryErrorScreen(
                text = "Failed to retrieve gym locations.",
                modifier = modifier,
                onClick = { gymLocationsViewModel.fetchGymLocations() },
            )
        }

        is GymLocationsUiState.Success -> {
            GymLocationsSelection(
                modifier = modifier,
                innerPadding = contentPadding,
                gyms = state.gymLocations.toImmutableList(),
                onClick = { onNavigateTo(it) },
            )
        }

        is GymLocationsUiState.Loading -> {
            GymLocationsLoadingShimmer(innerPadding = contentPadding, modifier = modifier)
        }
    }
}
