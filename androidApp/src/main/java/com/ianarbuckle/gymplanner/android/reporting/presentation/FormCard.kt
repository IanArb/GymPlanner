package com.ianarbuckle.gymplanner.android.reporting.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.android.ui.common.LoadingButton
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun FormCard(
    machineNumber: String,
    description: String,
    isMachineNumberValid: Boolean,
    isDescriptionValid: Boolean,
    isImageBitMapValid: Boolean,
    hasMachineNumberInteracted: Boolean,
    hasDescriptionInteracted: Boolean,
    hasPhotoInteracted: Boolean,
    imageBitmap: ImageBitmap? = null,
    isLoading: Boolean = false,
    hasFailed: Boolean = false,
    modifier: Modifier = Modifier,
    onMachineNumberChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPhotoClick: () -> Unit,
    onSendClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            FormFields(
                machineNumber = machineNumber,
                description = description,
                isMachineNumberValid = isMachineNumberValid,
                isDescriptionValid = isDescriptionValid,
                hasMachineNumberInteracted = hasMachineNumberInteracted,
                hasDescriptionInteracted = hasDescriptionInteracted,
                onMachineNumberChange = onMachineNumberChange,
                onDescriptionChange = onDescriptionChange
            )

            Spacer(modifier = Modifier.padding(12.dp))

            ImagePlaceholder(
                imageBitmap = imageBitmap,
                isImageError = !isImageBitMapValid && hasPhotoInteracted) {
                onPhotoClick()
            }

            LoadingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = if (isLoading) "" else "Send",
                isLoading = isLoading,
                onClick = onSendClick
            )

            Spacer(modifier = Modifier.padding(4.dp))

            if (hasFailed) {
                Text(text ="Failed to send report", color = Color.Red)
            }

        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FormCardPreview() {
    GymAppTheme {
        Surface {
            FormCard(
                machineNumber = "123",
                description = "This is a description",
                isMachineNumberValid = true,
                isDescriptionValid = true,
                isImageBitMapValid = true,
                hasMachineNumberInteracted = false,
                hasDescriptionInteracted = false,
                hasPhotoInteracted = false,
                imageBitmap = null,
                isLoading = false,
                hasFailed = false,
                onMachineNumberChange = {},
                onDescriptionChange = {},
                onPhotoClick = {},
                onSendClick = {}
            )
        }
    }
}