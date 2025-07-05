package com.ianarbuckle.gymplanner.android.personaltrainers.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
fun PersonalTrainersDetailScreen(
  contentPadding: PaddingValues,
  name: String,
  bio: String,
  imageUrl: String,
  onNavigateTo: () -> Unit,
  onBookClick: () -> Unit,
) {
  PersonalTrainersDetail(
    contentPadding = contentPadding,
    name = name,
    bio = bio,
    imageUrl = imageUrl,
    onBookClick = { onBookClick() },
    onBackClick = { onNavigateTo() },
  )
}
