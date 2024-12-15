package com.ianarbuckle.gymplanner.android.reporting.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun FormFields(
    machineNumber: String,
    description: String,
    isMachineNumberValid: Boolean,
    isDescriptionValid: Boolean,
    hasMachineNumberInteracted: Boolean,
    hasDescriptionInteracted: Boolean,
    modifier: Modifier = Modifier,
    onMachineNumberChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
) {
    Text(
        text = "Please provide the following information",
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        fontStyle = FontStyle.Normal
    )

    Spacer(modifier = modifier.padding(12.dp))

    Row {
        Text(
            text = "Machine number",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.padding(2.dp))
        Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
    }

    Spacer(modifier = Modifier.padding(2.dp))

    OutlinedTextField(
        value = machineNumber,
        onValueChange = onMachineNumberChange,
        label = { Text("Machine number") },
        modifier = modifier.fillMaxWidth(),
        isError = !isMachineNumberValid && hasMachineNumberInteracted,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    if (!isMachineNumberValid && hasMachineNumberInteracted) {
        Spacer(modifier = modifier.padding(2.dp))
        Text(
            text = "Please provide a machine number",
            color = MaterialTheme.colorScheme.error,
            fontStyle = FontStyle.Italic
        )
    }

    Spacer(modifier = modifier.padding(6.dp))

    Text(
        text = "Description",
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = modifier.padding(2.dp))

    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = { Text("Describe the fault of the machine") },
        modifier = Modifier.fillMaxWidth(),
        isError = !isDescriptionValid && hasDescriptionInteracted
    )

    if (!isDescriptionValid && hasDescriptionInteracted) {
        Spacer(modifier = modifier.padding(2.dp))
        Text(
            text = "Please provide a description",
            color = MaterialTheme.colorScheme.error,
            fontStyle = FontStyle.Italic
        )
    }

    Spacer(modifier = modifier.padding(12.dp))
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FormFieldsPreview() {
    GymAppTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            FormFields(
                machineNumber = "123",
                description = "This is a description",
                isMachineNumberValid = true,
                isDescriptionValid = true,
                hasMachineNumberInteracted = false,
                hasDescriptionInteracted = false,
                onMachineNumberChange = { },
                onDescriptionChange = { },
            )
        }
    }
}