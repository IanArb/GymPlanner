package com.ianarbuckle.gymplanner.android.navigation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.navigation.AvailabilityScreen
import com.ianarbuckle.gymplanner.android.navigation.BookingScreen
import com.ianarbuckle.gymplanner.android.navigation.ConversationScreen
import com.ianarbuckle.gymplanner.android.navigation.DashboardScreen
import com.ianarbuckle.gymplanner.android.navigation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.navigation.PersonalTrainersScreen
import com.ianarbuckle.gymplanner.android.navigation.ReportMachineBroken
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopNavigationBar(
    currentDestination: Any?,
    modifier: Modifier = Modifier,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    onBackClick: (() -> Unit)? = null,
) {
    val title =
        when (currentDestination) {
            is DashboardScreen -> stringResource(id = R.string.title_dashboard)
            is ReportMachineBroken -> stringResource(id = R.string.title_report_machine)
            is GymLocationsScreen -> stringResource(id = R.string.title_gym_locations)
            is PersonalTrainersScreen -> stringResource(id = R.string.title_personal_trainers)
            is AvailabilityScreen -> stringResource(id = R.string.title_availability)
            is BookingScreen -> stringResource(id = R.string.title_booking)
            is ConversationScreen -> stringResource(id = R.string.title_chat)
            else -> "" // Default or empty title
        }

    val enableBackButton =
        when (currentDestination) {
            is PersonalTrainersScreen,
            is AvailabilityScreen,
            is BookingScreen,
            is ConversationScreen -> true
            else -> false
        }

    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, color = titleColor, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            if (enableBackButton) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                    )
                }
            }
        },
    )
}

@PreviewsCombined
@Composable
private fun NavigationBarPreview() {
    GymAppTheme { TopNavigationBar(currentDestination = DashboardScreen, onBackClick = {}) }
}
