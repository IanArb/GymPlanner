package com.ianarbuckle.gymplanner.android.reporting.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.android.core.navigation.BottomNavigationItem
import com.ianarbuckle.gymplanner.android.commonUi.GymPlannerTheme
import com.ianarbuckle.gymplanner.android.commonUi.LoadingButton
import com.ianarbuckle.gymplanner.android.core.utils.imageBitmapToUri
import com.ianarbuckle.gymplanner.model.FaultReport
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ReportingFormContent(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap? = null,
    isLoading: Boolean = false,
    hasFailed: Boolean = false,
    onPhotoClick: () -> Unit,
    onSendClick: (FaultReport) -> Unit,
) {
    var machineNumber by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var isMachineNumberValid by remember { mutableStateOf(false) }
    var isDescriptionValid by remember { mutableStateOf(false) }
    var isImageBitMapValid by remember { mutableStateOf(false) }

    var hasMachineNumberInteracted by remember { mutableStateOf(false) }
    var hasDescriptionInteracted by remember { mutableStateOf(false) }
    var hasPhotoInteracted by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .padding(innerPadding)
        .padding(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
        ) {
            Column(modifier = modifier.padding(16.dp)) {
                Text(
                    text = "Please provide the following information",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal
                )

                Spacer(modifier = Modifier.padding(12.dp))

                Row {
                    Text(
                        text = "Machine number",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.padding(2.dp))
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
                }

                Spacer(modifier = Modifier.padding(2.dp))

                OutlinedTextField(
                    value = machineNumber,
                    onValueChange = {
                        machineNumber = it
                        isMachineNumberValid = it.isNotEmpty()
                        hasMachineNumberInteracted = true
                    },
                    label = { Text("Machine number") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isMachineNumberValid && hasMachineNumberInteracted,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                if (!isMachineNumberValid && hasMachineNumberInteracted) {
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Please provide a machine number",
                        color = Color.Red,
                        fontStyle = FontStyle.Italic
                    )
                }

                Spacer(modifier = Modifier.padding(6.dp))

                Text(
                    text = "Description",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.padding(2.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        isDescriptionValid = it.isNotEmpty()
                        hasDescriptionInteracted = true
                    },
                    label = { Text("Describe the fault of the machine") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isDescriptionValid && hasDescriptionInteracted
                )

                if (!isDescriptionValid && hasDescriptionInteracted) {
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Please provide a description",
                        color = Color.Red,
                        fontStyle = FontStyle.Italic
                    )
                }

                Spacer(modifier = Modifier.padding(12.dp))

                ImagePlaceholder(
                    imageBitmap = imageBitmap,
                    isImageError = !isImageBitMapValid && hasPhotoInteracted) {
                    hasPhotoInteracted = true
                    onPhotoClick()
                }

                val context = LocalContext.current

                LoadingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    text = if (isLoading) "" else "Send",
                    isLoading = isLoading,
                    onClick = {
                        hasMachineNumberInteracted = true
                        hasDescriptionInteracted = true
                        hasPhotoInteracted = true

                        isMachineNumberValid = machineNumber.isNotEmpty()
                        isDescriptionValid = description.isNotEmpty()
                        isImageBitMapValid = imageBitmap != null

                        if (isMachineNumberValid && isDescriptionValid && isImageBitMapValid) {
                            val report = FaultReport(
                                machineNumber = machineNumber.toInt(),
                                description = description,
                                photoUri = imageBitmap?.imageBitmapToUri(context, "photo.jgp").toString(),
                                date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
                            )
                            onSendClick(report)
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(4.dp))
                
                if (hasFailed) {
                    Text(text ="Failed to send report", color = Color.Red)
                }

            }
        }
    }
}

@Composable
fun ImagePlaceholder(
    imageBitmap: ImageBitmap?,
    isImageError: Boolean,
    onPhotoClick: () -> Unit,
) {

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    } else {
        ImageSelection {
            onPhotoClick()
        }

        if (isImageError) {
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "Please provide a photo",
                color = Color.Red,
                fontStyle = FontStyle.Italic
            )
        }
    }


}

@Composable
private fun ImageSelection(onPhotoClick: () -> Unit) {
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
            .clickable {
                onPhotoClick()
            },
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        Icon(
            imageVector = Icons.Filled.AddCircle,
            contentDescription = "Add photo"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Preview() {
    val navigationItems = listOf(
        BottomNavigationItem(
            title = "Dashboard",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavigationItem(
            title = "Chat",
            selectedIcon = Icons.Filled.Email,
            unselectedIcon = Icons.Outlined.Email,
        ),
        BottomNavigationItem(
            title = "Trainers",
            selectedIcon = Icons.Filled.Face,
            unselectedIcon = Icons.Outlined.Face,
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    GymPlannerTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Gym Plan") }) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    // TODO
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                }
            },
            bottomBar = {
                NavigationBar {
                    navigationItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                            },
                            label = {
                                Text(item.title)
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.title)
                            }
                        )
                    }
                }
            }
        ) {
            ReportingFormContent(
                innerPadding = it,
                imageBitmap = null,
                onPhotoClick = {

                },
                onSendClick = { data ->
                }
            )
        }
    }
}

