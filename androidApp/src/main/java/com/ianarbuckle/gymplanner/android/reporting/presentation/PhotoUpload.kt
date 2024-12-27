package com.ianarbuckle.gymplanner.android.reporting.presentation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun ImagePlaceholder(
    imageBitmap: ImageBitmap?,
    isImageError: Boolean,
    modifier: Modifier = Modifier,
    onPhotoClick: () -> Unit,
) {

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Photo",
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    } else {
        ImageSelection {
            onPhotoClick()
        }

        if (isImageError) {
            Spacer(modifier = modifier.padding(2.dp))
            Text(
                text = "Please provide a photo",
                color = Color.Red,
                fontStyle = FontStyle.Italic
            )
        }
    }


}

@Composable
private fun ImageSelection(
    modifier: Modifier = Modifier,
    onPhotoClick: () -> Unit,
) {
    Text(
        text = "Take a photo",
        color = Color.Black,
        fontWeight = FontWeight.Bold,
    )

    Spacer(modifier = modifier.padding(2.dp))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray)
            .padding(16.dp)
            .testTag(ImageSelectionTestTag)
            .clickable {
                onPhotoClick()
            },
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = modifier.padding(8.dp))
        Icon(
            imageVector = Icons.Filled.AddCircle,
            contentDescription = "Add photo"
        )
    }
}

const val ImageSelectionTestTag = "ImageSelection"


@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ImagePlaceholderPreview() {
    GymAppTheme {
        Surface {
            ImagePlaceholder(
                imageBitmap = null,
                isImageError = false,
                onPhotoClick = {}
            )
        }
    }
}