package com.ianarbuckle.gymplanner.android.reporting.presentation

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.reporting.data.FormFaultReportUiState
import com.ianarbuckle.gymplanner.android.reporting.data.ReportingViewModel
import com.ianarbuckle.gymplanner.android.utils.imageBitmapToUri
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport

@Suppress("LongMethod")
@Composable
fun ReportMachineBrokenScreen(
  contentPadding: PaddingValues,
  reportingViewModel: ReportingViewModel = hiltViewModel(),
) {
  var machineNumber by rememberSaveable { mutableStateOf("") }
  var description by rememberSaveable { mutableStateOf("") }
  var imageBitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }
  var isLoading by rememberSaveable { mutableStateOf(false) }
  var hasFailed by rememberSaveable { mutableStateOf(false) }
  var isMachineNumberValid by rememberSaveable { mutableStateOf(false) }
  var isDescriptionValid by rememberSaveable { mutableStateOf(false) }
  var isImageBitMapValid by rememberSaveable { mutableStateOf(false) }
  var hasMachineNumberInteracted by rememberSaveable { mutableStateOf(false) }
  var hasDescriptionInteracted by rememberSaveable { mutableStateOf(false) }
  var hasPhotoInteracted by rememberSaveable { mutableStateOf(false) }

  val launchCamera =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
      bitmap ->
      imageBitmap = bitmap
    }

  val context = LocalContext.current

  when (val value = reportingViewModel.uiState.collectAsState().value) {
    is FormFaultReportUiState.FormLoading -> {
      ReportingFormContent(
        innerPadding = contentPadding,
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
        imageBitmap = imageBitmap?.asImageBitmap(),
        isLoading = true,
        hasFailed = hasFailed,
        isMachineNumberValid = isMachineNumberValid,
        isDescriptionValid = isDescriptionValid,
        isImageBitMapValid = isImageBitMapValid,
        hasMachineNumberInteracted = hasMachineNumberInteracted,
        hasDescriptionInteracted = hasDescriptionInteracted,
        hasPhotoInteracted = hasPhotoInteracted,
        onPhotoClick = {
          hasPhotoInteracted = true
          launchCamera.launch()
        },
        onSendClick = {},
      )
    }

    is FormFaultReportUiState.FormSuccess -> {
      ReportingFormResponse(
        innerPadding = contentPadding,
        faultReport = value.data,
        onClick = {
          machineNumber = ""
          description = ""
          imageBitmap = null
          isLoading = false
          hasFailed = false
          isMachineNumberValid = false
          isDescriptionValid = false
          isImageBitMapValid = false
          hasMachineNumberInteracted = false
          hasDescriptionInteracted = false
          hasPhotoInteracted = false
        },
      )
    }

    is FormFaultReportUiState.FormError -> {
      ReportingFormContent(
        innerPadding = contentPadding,
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
        imageBitmap = imageBitmap?.asImageBitmap(),
        isLoading = false,
        hasFailed = true,
        isMachineNumberValid = isMachineNumberValid,
        isDescriptionValid = isDescriptionValid,
        isImageBitMapValid = isImageBitMapValid,
        hasMachineNumberInteracted = hasMachineNumberInteracted,
        hasDescriptionInteracted = hasDescriptionInteracted,
        hasPhotoInteracted = hasPhotoInteracted,
        onPhotoClick = {
          hasPhotoInteracted = true
          launchCamera.launch()
        },
        onSendClick = {
          hasMachineNumberInteracted = true
          hasDescriptionInteracted = true
          hasPhotoInteracted = true

          isMachineNumberValid = machineNumber.isNotEmpty()
          isDescriptionValid = description.isNotEmpty()
          isImageBitMapValid = imageBitmap != null

          if (isMachineNumberValid && isDescriptionValid && isImageBitMapValid) {
            reportingViewModel.submitFault(
              FaultReport(
                machineNumber = machineNumber.toInt(),
                description = description,
                photoUri =
                  imageBitmap
                    ?.asImageBitmap()
                    ?.imageBitmapToUri(context, "machine_fault_report-$machineNumber.png")
                    .toString(),
                date = System.currentTimeMillis().toString(),
              )
            )
          }
        },
      )
    }

    FormFaultReportUiState.FormIdle -> {
      ReportingFormContent(
        innerPadding = contentPadding,
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
        imageBitmap = imageBitmap?.asImageBitmap(),
        isLoading = false,
        hasFailed = hasFailed,
        isMachineNumberValid = isMachineNumberValid,
        isDescriptionValid = isDescriptionValid,
        isImageBitMapValid = isImageBitMapValid,
        hasMachineNumberInteracted = hasMachineNumberInteracted,
        hasDescriptionInteracted = hasDescriptionInteracted,
        hasPhotoInteracted = hasPhotoInteracted,
        onPhotoClick = {
          hasPhotoInteracted = true
          launchCamera.launch()
        },
        onSendClick = {
          hasMachineNumberInteracted = true
          hasDescriptionInteracted = true
          hasPhotoInteracted = true

          isMachineNumberValid = machineNumber.isNotEmpty()
          isDescriptionValid = description.isNotEmpty()
          isImageBitMapValid = imageBitmap != null

          if (isMachineNumberValid && isDescriptionValid && isImageBitMapValid) {
            reportingViewModel.submitFault(
              FaultReport(
                machineNumber = machineNumber.toInt(),
                description = description,
                photoUri =
                  imageBitmap
                    ?.asImageBitmap()
                    ?.imageBitmapToUri(context, "machine_fault_report-$machineNumber.png")
                    .toString(),
                date = System.currentTimeMillis().toString(),
              )
            )
          }
        },
      )
    }
  }
}
