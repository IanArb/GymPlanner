package com.ianarbuckle.gymplanner.android.personaltrainers.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PersonalTrainersContent(
    innerPadding: PaddingValues,
    personalTrainers: ImmutableList<PersonalTrainer>,
    onPersonalTrainerSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

}

@Composable
@Preview
fun GymLocationsPreview() {

}