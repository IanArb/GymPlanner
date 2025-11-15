package com.ianarbuckle.gymplanner.android.personaltrainers.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ianarbuckle.gymplanner.R
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined

@Composable
fun PersonalTrainersDetail(
    contentPadding: PaddingValues,
    name: String,
    bio: String,
    imageUrl: String,
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(contentPadding)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(300.dp),
            )

            // Added a scrim to ensure the text has enough contrast to be legible.
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .height(300.dp)
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                )
                        )
            )

            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                // Fixed the double padding and increased it for better spacing.
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
            )

            IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(
                    imageVector = ImageVector.vectorResource(com.ianarbuckle.gymplanner.android.R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Color.White,
                )
            }
        }

        Text(
            text = bio,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            Button(
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).fillMaxWidth(),
                onClick = { onBookClick() },
            ) {
                Text(text = "Book now")
            }
        }
    }
}

@PreviewsCombined
@Composable
private fun PersonalTrainerDetailPreview() {
    GymAppTheme {
        // Used MaterialTheme.colorScheme.surface to make the background theme-aware.
        Column(modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.surface)) {
            val personalTrainer = DataProvider.personalTrainers().first()
            PersonalTrainersDetail(
                contentPadding = PaddingValues(0.dp), // Padding is already on the Column
                imageUrl = personalTrainer.imageUrl,
                name = personalTrainer.firstName + " " + personalTrainer.lastName,
                bio = personalTrainer.bio,
                onBookClick = {},
                onBackClick = {},
            )
        }
    }
}
