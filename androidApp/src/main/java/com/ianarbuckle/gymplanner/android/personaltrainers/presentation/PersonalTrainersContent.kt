package com.ianarbuckle.gymplanner.android.personaltrainers.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.ui.theme.surfaceLight
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PersonalTrainersContent(
    innerPadding: PaddingValues,
    personalTrainers: ImmutableList<PersonalTrainer>,
    modifier: Modifier = Modifier,
    onSocialLinkClick: (String) -> Unit,
    onBookTrainerClick: (String) -> Unit,
    onItemClick: (Triple<String, String, String>) -> Unit,
) {
    Column(
        modifier = modifier.padding(innerPadding)
    ) {
        LazyColumn {
            items(personalTrainers) { personalTrainer ->
                PersonalTrainerItem(
                    personalTrainer = personalTrainer,
                    onSocialLinkClick = onSocialLinkClick,
                    onBookTrainerClick = onBookTrainerClick,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun PersonalTrainerItem(
    personalTrainer: PersonalTrainer,
    modifier: Modifier = Modifier,
    onSocialLinkClick: (String) -> Unit,
    onBookTrainerClick: (String) -> Unit,
    onItemClick: (Triple<String, String, String>) -> Unit,
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                onItemClick(
                    Triple(
                        personalTrainer.firstName + " " + personalTrainer.lastName,
                        personalTrainer.bio,
                        personalTrainer.imageUrl
                    )
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row {
                AsyncImage(
                    model = personalTrainer.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Column(modifier = modifier.padding(start = 16.dp)) {
                    Text(
                        text = personalTrainer.firstName + " " + personalTrainer.lastName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = modifier.padding(2.dp))
                    Text(
                        text = "Qualifications",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = personalTrainer.qualifications.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = modifier.padding(2.dp))

                    val socials = personalTrainer.socials

                    if (socials.isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(8.dp))

                        Text(
                            text = "Socials",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Row(modifier = Modifier.padding(top = 10.dp, end = 6.dp)) {
                                socials.entries.forEachIndexed { index, social ->
                                    when (social.key) {
                                        "instagram" -> {
                                            SocialImage(socials = social, drawable = R.drawable.ic_instagram) {
                                                onSocialLinkClick(social.value)
                                            }
                                        }
                                        "tiktok" -> {
                                            SocialImage(socials = social, drawable = R.drawable.ic_tiktok) {
                                                onSocialLinkClick(social.value)
                                            }
                                        }
                                    }
                                    if (index < socials.size - 1) {
                                        Spacer(modifier = Modifier.size(8.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    personalTrainer.id?.let {
                        onBookTrainerClick(it)
                    }
                }) {
                Text("Book now")
            }
        }
    }
}

@Composable
private fun SocialImage(
    socials: Map.Entry<String, String>,
    drawable: Int,
    modifier: Modifier = Modifier,
    onSocialLinkClick: (String) -> Unit
) {
    Image(
        painter = painterResource(id = drawable),
        contentDescription = socials.value,
        modifier = modifier
            .size(24.dp)
            .clickable {
                onSocialLinkClick(socials.value)
            }
    )
}

@Composable
@Preview
fun GymLocationsPreview() {
    GymAppTheme {
        Column(
            modifier =
            Modifier
                .padding(16.dp)
                .background(surfaceLight)
        ) {
            PersonalTrainersContent(
                innerPadding = PaddingValues(16.dp),
                personalTrainers = DataProvider.personalTrainers(),
                onSocialLinkClick = { },
                onBookTrainerClick = { },
                onItemClick = { }
            )
        }

    }
}