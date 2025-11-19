package com.ianarbuckle.gymplanner.android.personaltrainers.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersUiState
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.collections.immutable.toImmutableList

@Composable
fun PersonalTrainersScreen(
    contentPadding: PaddingValues,
    gymLocation: GymLocation,
    onNavigateTo: (Triple<String, String, String>) -> Unit,
    onBookClick: (PersonalTrainer) -> Unit,
    modifier: Modifier = Modifier,
    personalTrainersViewModel: PersonalTrainersViewModel = hiltViewModel(),
) {
    LaunchedEffect(true) { personalTrainersViewModel.fetchPersonalTrainers(gymLocation) }

    when (val uiState = personalTrainersViewModel.uiState.collectAsState().value) {
        is PersonalTrainersUiState.Failure -> {
            RetryErrorScreen(
                text = "Failed to retrieve personal trainers.",
                onClick = { personalTrainersViewModel.fetchPersonalTrainers(gymLocation) },
            )
        }

        is PersonalTrainersUiState.Success -> {
            PersonalTrainersContent(
                modifier = modifier,
                innerPadding = contentPadding,
                personalTrainers = uiState.personalTrainers.toImmutableList(),
                onSocialLinkClick = {},
                onBookTrainerClick = { onBookClick(it) },
                onItemClick = { item -> onNavigateTo(Triple(item.first, item.second, item.third)) },
            )
        }

        is PersonalTrainersUiState.Loading -> {
            PersonalTrainersLoadingShimmer(innerPadding = contentPadding, modifier = modifier)
        }
    }
}
