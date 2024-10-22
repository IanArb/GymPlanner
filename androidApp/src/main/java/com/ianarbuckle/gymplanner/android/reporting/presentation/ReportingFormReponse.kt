package com.ianarbuckle.gymplanner.android.reporting.presentation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.ianarbuckle.gymplanner.android.core.navigation.BottomNavigationItem
import com.ianarbuckle.gymplanner.android.core.theme.GymAppTheme
import com.ianarbuckle.gymplanner.model.FaultReport

@Composable
fun ReportingFormResponse(
    innerPadding: PaddingValues,
    faultReport: FaultReport,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
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
                    text = "Your report",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal
                )

                Spacer(modifier = Modifier.padding(12.dp))

                Text(
                    text = "Machine number",
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.padding(2.dp))

                Spacer(modifier = Modifier.padding(2.dp))

                Text(
                    text = faultReport.machineNumber.toString(),
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.padding(6.dp))

                Text(
                    text = "Description",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.padding(2.dp))

                Text(
                    text = faultReport.description,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.padding(12.dp))

                ImageFromUri(
                    uri = Uri.parse(faultReport.photoUri),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.padding(6.dp))

                Button(
                    onClick = { onClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Report again",
                    )
                }
            }
        }
    }
}

@Composable
fun ImageFromUri(uri: Uri, modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(uri),
        contentDescription = "Photo",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ReportingFormResponsePreview() {
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

    GymAppTheme {
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
            ReportingFormResponse(innerPadding = it, faultReport = FaultReport(
                machineNumber = 123,
                description = "The machine is broken",
                photoUri = "https://www.google.com",
                date = "2022-01-01"
            )) {

            }
        }
    }
}