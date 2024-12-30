package com.ianarbuckle.gymplanner.android.personaltrainers.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.ui.theme.surfaceLight
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PersonalTrainersContent(
    innerPadding: PaddingValues,
    personalTrainers: ImmutableList<PersonalTrainer>,
    onSocialLinkClick: (String) -> Unit,
    onBookTrainerClick: (PersonalTrainer) -> Unit,
    onItemClick: (Triple<String, String, String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(innerPadding),
    ) {
        LazyColumn(
            modifier = Modifier.testTag(PersonalTrainersItemsTag),
        ) {
            items(personalTrainers) { personalTrainer ->
                PersonalTrainerItem(
                    personalTrainer = personalTrainer,
                    onSocialLinkClick = onSocialLinkClick,
                    onBookTrainerClick = onBookTrainerClick,
                    onItemClick = onItemClick,
                )
            }
        }
    }
}

const val PersonalTrainersItemsTag = "PersonalTrainersItemsTag"

@Composable
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun GymLocationsPreview() {
    GymAppTheme {
        Column(
            modifier =
            Modifier
                .padding(16.dp)
                .background(surfaceLight),
        ) {
            PersonalTrainersContent(
                innerPadding = PaddingValues(16.dp),
                personalTrainers = DataProvider.personalTrainers(),
                onSocialLinkClick = { },
                onBookTrainerClick = { },
                onItemClick = { },
            )
        }
    }
}
