package com.ianarbuckle.gymplanner.android.reporting.presentation

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.common.LoadingButton
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined

@Suppress("LongParameterList", "LongMethod")
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
    onPhotoClick: () -> Unit,
    onSendClick: () -> Unit,
    onMachineNumberChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    hasFailed: Boolean = false,
    imageBitmap: ImageBitmap? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FormFields(
                machineNumber = machineNumber,
                description = description,
                isMachineNumberValid = isMachineNumberValid,
                isDescriptionValid = isDescriptionValid,
                hasMachineNumberInteracted = hasMachineNumberInteracted,
                hasDescriptionInteracted = hasDescriptionInteracted,
                onMachineNumberChange = onMachineNumberChange,
                onDescriptionChange = onDescriptionChange,
                modifier = Modifier,
            )

            ImagePlaceholder(
                imageBitmap = imageBitmap,
                isImageError = !isImageBitMapValid && hasPhotoInteracted,
                onPhotoClick = onPhotoClick,
            )

            LoadingButton(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                text = if (isLoading) "" else "Send",
                isLoading = isLoading,
                onClick = onSendClick,
            )

            Spacer(modifier = Modifier.padding(4.dp))

            if (hasFailed) {
                Text(text = "Failed to send report", color = Color.Red)
            }
        }
    }
}

@PreviewsCombined
@Composable
private fun FormCardPreview() {
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
                onSendClick = {},
            )
        }
    }
}
