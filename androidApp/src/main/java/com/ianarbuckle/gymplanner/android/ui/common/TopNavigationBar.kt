package com.ianarbuckle.gymplanner.android.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ianarbuckle.gymplanner.android.navigation.AvailabilityScreen
import com.ianarbuckle.gymplanner.android.navigation.DashboardScreen
import com.ianarbuckle.gymplanner.android.navigation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.navigation.PersonalTrainersScreen
import com.ianarbuckle.gymplanner.android.navigation.ReportMachineBroken
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopNavigationBar(
    currentRoute: String?,
    modifier: Modifier = Modifier,
    titleColor: Color = Color.Black,
    enableBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = when (currentRoute) {
                    DashboardScreen::class.qualifiedName -> "Dashboard"
                    ReportMachineBroken::class.qualifiedName -> "Report Machine"
                    GymLocationsScreen::class.qualifiedName -> "Gym Locations"
                    PersonalTrainersScreen::class.qualifiedName
                        .plus("/{gymLocation}"),
                    -> "Personal Trainers"
                    AvailabilityScreen::class.qualifiedName
                        .plus(("/{name}/{imageUrl}?qualifications={qualifications}")),
                    -> "Book Trainer"
                    else -> ""
                },
                color = titleColor,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            if (enableBackButton) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun NavigationBarPreview() {
    GymAppTheme {
        TopNavigationBar(
            currentRoute = DashboardScreen::class.qualifiedName,
            enableBackButton = true,
            onBackClick = {
            },
        )
    }
}
