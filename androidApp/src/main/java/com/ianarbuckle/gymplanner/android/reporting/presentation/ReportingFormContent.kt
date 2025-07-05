package com.ianarbuckle.gymplanner.android.reporting.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Suppress("LongParameterList")
@Composable
fun ReportingFormContent(
  innerPadding: PaddingValues,
  machineNumber: String,
  description: String,
  isMachineNumberValid: Boolean,
  isDescriptionValid: Boolean,
  isImageBitMapValid: Boolean,
  hasMachineNumberInteracted: Boolean,
  hasDescriptionInteracted: Boolean,
  hasPhotoInteracted: Boolean,
  onMachineNumberChange: (String) -> Unit,
  onDescriptionChange: (String) -> Unit,
  onPhotoClick: () -> Unit,
  onSendClick: () -> Unit,
  modifier: Modifier = Modifier,
  isLoading: Boolean = false,
  hasFailed: Boolean = false,
  imageBitmap: ImageBitmap? = null,
) {
  Column(modifier = modifier.padding(innerPadding).padding(16.dp)) {
    FormCard(
      machineNumber = machineNumber,
      description = description,
      isMachineNumberValid = isMachineNumberValid,
      isDescriptionValid = isDescriptionValid,
      isImageBitMapValid = isImageBitMapValid,
      hasMachineNumberInteracted = hasMachineNumberInteracted,
      hasDescriptionInteracted = hasDescriptionInteracted,
      hasPhotoInteracted = hasPhotoInteracted,
      imageBitmap = imageBitmap,
      isLoading = isLoading,
      hasFailed = hasFailed,
      onMachineNumberChange = onMachineNumberChange,
      onDescriptionChange = onDescriptionChange,
      onPhotoClick = onPhotoClick,
      onSendClick = onSendClick,
      modifier = Modifier,
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ReportingFormContentPreview() {
  var machineNumber by rememberSaveable { mutableStateOf("") }
  var description by rememberSaveable { mutableStateOf("") }
  val imageBitmap by rememberSaveable { mutableStateOf<ImageBitmap?>(null) }
  val isLoading by rememberSaveable { mutableStateOf(false) }
  val hasFailed by rememberSaveable { mutableStateOf(false) }
  var isMachineNumberValid by rememberSaveable { mutableStateOf(false) }
  var isDescriptionValid by rememberSaveable { mutableStateOf(false) }
  var isImageBitMapValid by rememberSaveable { mutableStateOf(false) }
  var hasMachineNumberInteracted by rememberSaveable { mutableStateOf(false) }
  var hasDescriptionInteracted by rememberSaveable { mutableStateOf(false) }
  var hasPhotoInteracted by rememberSaveable { mutableStateOf(false) }

  GymAppTheme {
    Scaffold(topBar = { TopAppBar(title = { Text("Report Machine") }) }) { innerPadding ->
      ReportingFormContent(
        innerPadding = innerPadding,
        machineNumber = machineNumber,
        onMachineNumberChange = {
          machineNumber = it
          isMachineNumberValid = it.isNotEmpty()
          hasMachineNumberInteracted = true
        },
        description = description,
        onDescriptionChange = {
          description = it
          isDescriptionValid = it.isNotEmpty()
          hasDescriptionInteracted = true
        },
        imageBitmap = imageBitmap,
        isLoading = isLoading,
        hasFailed = hasFailed,
        isMachineNumberValid = isMachineNumberValid,
        isDescriptionValid = isDescriptionValid,
        isImageBitMapValid = isImageBitMapValid,
        hasMachineNumberInteracted = hasMachineNumberInteracted,
        hasDescriptionInteracted = hasDescriptionInteracted,
        hasPhotoInteracted = hasPhotoInteracted,
        onPhotoClick = {
          hasPhotoInteracted = true
          // Handle photo click
        },
        onSendClick = {
          hasMachineNumberInteracted = true
          hasDescriptionInteracted = true
          hasPhotoInteracted = true

          isMachineNumberValid = machineNumber.isNotEmpty()
          isDescriptionValid = description.isNotEmpty()
          isImageBitMapValid = imageBitmap != null

          if (isMachineNumberValid && isDescriptionValid && isImageBitMapValid) {
            // Handle send click
          }
        },
      )
    }
  }
}
