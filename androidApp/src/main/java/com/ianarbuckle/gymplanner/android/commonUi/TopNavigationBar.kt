package com.ianarbuckle.gymplanner.android.commonUi

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ianarbuckle.gymplanner.android.core.navigation.DashboardScreen
import com.ianarbuckle.gymplanner.android.core.navigation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.core.navigation.PersonalTrainersScreen
import com.ianarbuckle.gymplanner.android.core.navigation.ReportMachineBroken

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopNavigationBar(
    currentRoute: String?,
    titleColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    enableBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            Text(
                text = when (currentRoute) {
                    DashboardScreen::class.qualifiedName -> "Dashboard"
                    ReportMachineBroken::class.qualifiedName -> "Report Machine"
                    GymLocationsScreen::class.qualifiedName -> "Personal Trainers"
                    PersonalTrainersScreen::class.qualifiedName -> "Personal Trainers Profiles"
                    else -> "Gym Plan"
                },
                color = titleColor,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor
        ),
        navigationIcon = {
            if (enableBackButton) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun NavigationBarPreview() {
    GymPlannerTheme {
        TopNavigationBar(
            currentRoute = DashboardScreen::class.qualifiedName,
            enableBackButton = true,
            ) {
            // No-op
        }
    }
}