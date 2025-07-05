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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.ui.theme.surfaceLight
import com.ianarbuckle.gymplanner.android.utils.DataProvider

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

      Text(
        text = name,
        style = MaterialTheme.typography.titleLarge,
        color = Color.White,
        modifier = Modifier.align(Alignment.BottomStart).padding(8.dp).padding(8.dp),
      )

      IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = Color.White,
        )
      }
    }

    Text(text = bio, modifier = Modifier.padding(16.dp))

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

@Preview
@Composable
private fun PersonalTrainerDetailPreview() {
  GymAppTheme {
    Column(modifier = Modifier.padding(16.dp).background(surfaceLight)) {
      val personalTrainer = DataProvider.personalTrainers().first()
      PersonalTrainersDetail(
        contentPadding = PaddingValues(16.dp),
        imageUrl = personalTrainer.imageUrl,
        name = personalTrainer.firstName + " " + personalTrainer.lastName,
        bio = personalTrainer.bio,
        onBookClick = {},
        onBackClick = {},
      )
    }
  }
}
