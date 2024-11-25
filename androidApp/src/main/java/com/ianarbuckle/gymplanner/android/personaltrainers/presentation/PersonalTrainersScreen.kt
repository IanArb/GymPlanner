package com.ianarbuckle.gymplanner.android.personaltrainers.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersState
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.collections.immutable.toImmutableList

@Composable
fun PersonalTrainersScreen(
    personalTrainersViewModel: PersonalTrainersViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    gymLocation: GymLocation,
    onNavigateTo: (Triple<String, String, String>) -> Unit,
) {
    LaunchedEffect(true) {
        personalTrainersViewModel.fetchPersonalTrainers(gymLocation)
    }

    when (val uiState = personalTrainersViewModel.uiState.collectAsState().value) {
        is PersonalTrainersState.Failure -> {
            RetryErrorScreen(
                text = "Failed to retrieve personal trainers."
            ) {
                personalTrainersViewModel.fetchPersonalTrainers(gymLocation)
            }
        }

        is PersonalTrainersState.Success -> {
            PersonalTrainersContent(
                innerPadding = contentPadding,
                personalTrainers = uiState.personalTrainers.toImmutableList(),
                onSocialLinkClick = {

                },
                onBookTrainerClick = { },
                onItemClick = { item ->
                    onNavigateTo(
                        Triple(
                            item.first,
                            item.second,
                            item.third
                        )
                    )
                }
            )
        }

        is PersonalTrainersState.Loading -> {
            CircularProgressIndicator()
        }
    }
}