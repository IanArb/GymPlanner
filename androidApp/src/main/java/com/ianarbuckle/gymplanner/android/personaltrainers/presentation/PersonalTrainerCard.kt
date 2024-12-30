package com.ianarbuckle.gymplanner.android.personaltrainers.presentation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.ui.common.Avatar
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

@Composable
fun PersonalTrainerItem(
    personalTrainer: PersonalTrainer,
    onSocialLinkClick: (String) -> Unit,
    onBookTrainerClick: (PersonalTrainer) -> Unit,
    onItemClick: (Triple<String, String, String>) -> Unit,
    modifier: Modifier = Modifier,
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
                        personalTrainer.imageUrl,
                    ),
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Avatar(
                    imageUrl = personalTrainer.imageUrl,
                    contentDescription = personalTrainer.firstName,
                )

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = personalTrainer.firstName + " " + personalTrainer.lastName,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Qualifications",
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = personalTrainer.qualifications.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Spacer(modifier = Modifier.padding(2.dp))

                    val socials = personalTrainer.socials

                    if (socials.isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(8.dp))

                        Text(
                            text = "Socials",
                            style = MaterialTheme.typography.titleSmall,
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Row(modifier = Modifier.padding(top = 10.dp, end = 6.dp)) {
                                socials.entries.forEachIndexed { index, social ->
                                    when (social.key) {
                                        "instagram" -> {
                                            SocialImage(
                                                socials = social,
                                                drawable = R.drawable.ic_instagram,
                                                onSocialLinkClick = {
                                                    onSocialLinkClick(social.value)
                                                }
                                            )
                                        }
                                        "tiktok" -> {
                                            SocialImage(
                                                socials = social,
                                                drawable = R.drawable.ic_tiktok,
                                                onSocialLinkClick = {
                                                    onSocialLinkClick(social.value)
                                                }
                                            )
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
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onBookTrainerClick(personalTrainer)
                },
            ) {
                Text("Book now")
            }
        }
    }
}

@Composable
private fun SocialImage(
    socials: Map.Entry<String, String>,
    drawable: Int,
    onSocialLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = drawable),
        contentDescription = socials.value,
        modifier = modifier
            .size(24.dp)
            .clickable {
                onSocialLinkClick(socials.value)
            },
    )
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PersonalTrainerItemPreview() {
    GymAppTheme {
        Surface {
            PersonalTrainerItem(
                personalTrainer = PersonalTrainer(
                    firstName = "John",
                    lastName = "Doe",
                    bio = "Bio",
                    imageUrl = "https://www.example.com/image.jpg",
                    qualifications = listOf("Qualification 1", "Qualification 2"),
                    socials = mapOf("instagram" to "https://www.instagram.com", "tiktok" to "https://www.tiktok.com"),
                    gymLocation = GymLocation.CLONTARF,
                ),
                onSocialLinkClick = { },
                onBookTrainerClick = { },
                onItemClick = { },
            )
        }
    }
}
