package com.ianarbuckle.gymplanner.android.availability.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.common.Avatar
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined

@Composable
fun PersonalTrainerCard(
    personalTrainerLabel: String,
    name: String,
    imageUrl: String,
    qualifications: List<String>,
    isAvailable: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.padding(16.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            PersonalTrainer(
                personalTrainerLabel = personalTrainerLabel,
                name = name,
                imageUrl = imageUrl,
                qualifications = qualifications,
                isAvailable = isAvailable,
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun PersonalTrainer(
    personalTrainerLabel: String,
    name: String,
    imageUrl: String,
    qualifications: List<String>,
    isAvailable: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Avatar(imageUrl = imageUrl, contentDescription = "Avatar", isAvailable = isAvailable)

        Spacer(modifier = Modifier.padding(2.dp))

        Text(text = name, style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.padding(2.dp))

        Text(text = personalTrainerLabel, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            text = qualifications.joinToString(", "),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewsCombined
@Composable
private fun PersonalTrainerCardPreview() {
    GymAppTheme {
        Surface {
            PersonalTrainerCard(
                personalTrainerLabel = "Personal Trainer",
                name = "John Doe",
                imageUrl = "https://example.com/image.jpg",
                qualifications = listOf("qualification1", "qualification2"),
                isAvailable = true,
            )
        }
    }
}
