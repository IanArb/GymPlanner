package com.ianarbuckle.gymplanner.android.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined

@Composable
fun Avatar(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    isAvailable: Boolean = false,
) {
    Box(modifier = modifier.size(100.dp)) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp).clip(CircleShape),
            placeholder = painterResource(id = R.drawable.ic_placeholder),
        )
        if (isAvailable) {
            Box(
                modifier =
                    Modifier.size(16.dp)
                        .offset(x = (-16).dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                        .align(Alignment.BottomEnd)
            )
        }
    }
}

@PreviewsCombined
@Composable
private fun AvatarPreview() {
    Avatar(
        imageUrl = "https://www.example.com/image.jpg",
        contentDescription = "Avatar",
        isAvailable = true,
    )
}
