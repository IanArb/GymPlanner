package com.ianarbuckle.gymplanner.android.personaltrainers.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
fun PersonalTrainersDetailScreen(
    contentPadding: PaddingValues,
    onNavigateTo: () -> Unit,
    name: String,
    bio: String,
    imageUrl: String,
) {
    PersonalTrainersDetail(
        contentPadding = contentPadding,
        name = name,
        bio = bio,
        imageUrl = imageUrl,
        onBookClick = {},
        onBackClick = {
            onNavigateTo()
        }
    )
}