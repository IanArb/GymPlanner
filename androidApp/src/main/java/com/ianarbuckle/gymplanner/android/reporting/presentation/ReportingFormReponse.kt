package com.ianarbuckle.gymplanner.android.reporting.presentation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.navigation.BottomNavigationItem
import com.ianarbuckle.gymplanner.android.navigation.IconSource
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport

@Composable
fun ReportingFormResponse(
    innerPadding: PaddingValues,
    faultReport: FaultReport,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(innerPadding).padding(16.dp)) {
        FormResponseCard(faultReport = faultReport, onClick = { onClick() })
    }
}

@Composable
fun ImageFromUri(uri: Uri, modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(uri),
        contentDescription = "Photo",
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewsCombined
@Composable
private fun ReportingFormResponsePreview() {
    val navigationItems =
        listOf(
            BottomNavigationItem(
                title = "Dashboard",
                selectedIcon = IconSource.FromResource(R.drawable.ic_home_icon_filled),
                unselectedIcon = IconSource.FromResource(R.drawable.ic_home_icon_outlined),
            ),
            BottomNavigationItem(
                title = "Chat",
                selectedIcon = IconSource.FromResource(R.drawable.ic_gym_icon_filled),
                unselectedIcon = IconSource.FromResource(R.drawable.ic_gym_icon_outlined),
            ),
            BottomNavigationItem(
                title = "Trainers",
                selectedIcon = IconSource.FromResource(R.drawable.ic_groups_icon_filled),
                unselectedIcon = IconSource.FromResource(R.drawable.ic_groups_icon_outlined),
            ),
        )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    GymAppTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Gym Plan") }) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // TODO
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add_filled),
                        contentDescription = "Add",
                    )
                }
            },
            bottomBar = {
                NavigationBar {
                    navigationItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = { selectedItemIndex = index },
                            label = { Text(item.title) },
                            icon = {
                                Icon(
                                    imageVector =
                                        ImageVector.vectorResource(
                                            R.drawable.ic_groups_icon_outlined
                                        ),
                                    contentDescription = item.title,
                                )
                            },
                        )
                    }
                }
            },
        ) {
            ReportingFormResponse(
                innerPadding = it,
                faultReport =
                    FaultReport(
                        machineNumber = 123,
                        description = "The machine is broken",
                        photoUri = "https://www.google.com",
                        date = "2022-01-01",
                    ),
                onClick = {},
            )
        }
    }
}
