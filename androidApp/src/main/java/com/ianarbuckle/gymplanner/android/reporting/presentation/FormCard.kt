package com.ianarbuckle.gymplanner.android.reporting.presentation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.android.ui.common.LoadingButton
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
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

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                )
            } else {
                Text(
                    text = "Take a photo",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.padding(2.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.LightGray)
                        .padding(16.dp)
                        .testTag("ImageSelectionTestTag")
                        .clickable {
                            onPhotoClick()
                        },
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add photo",
                    )
                }

                if (isImageBitMapValid && hasPhotoInteracted) {
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Please provide a photo",
                        color = Color.Red,
                        fontStyle = FontStyle.Italic,
                    )
                }
            }

            LoadingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
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

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
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
