package com.ianarbuckle.gymplanner.android.gymlocations.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.collections.immutable.toImmutableList

@Composable
fun GymLocationsScreen(
    contentPadding: PaddingValues,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
    gymLocationsViewModel: GymLocationsViewModel = hiltViewModel(),
) {
    LaunchedEffect(true) { gymLocationsViewModel.fetchGymLocations() }

    when (val uiState = gymLocationsViewModel.uiState.collectAsState().value) {
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
                gyms = uiState.gymLocations.toImmutableList(),
                onClick = { onNavigateTo(it.title) },
            )
        }

        is GymLocationsUiState.Loading -> {
            GymLocationsLoadingShimmer(
                innerPadding = contentPadding,
                modifier = modifier,
                shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View),
            )
        }
    }
}

@Composable
@Preview
private fun GymLocationsScreenPreview() {
    GymLocationsScreen(contentPadding = PaddingValues(16.dp), onNavigateTo = {})
}
