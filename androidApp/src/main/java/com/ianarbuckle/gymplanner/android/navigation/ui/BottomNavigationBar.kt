package com.ianarbuckle.gymplanner.android.navigation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.navigation.DashboardScreen
import com.ianarbuckle.gymplanner.android.navigation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.navigation.ReportMachineBroken
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BottomNavigationBar(
    destinations: ImmutableList<Any>,
    currentDestination: Any?,
    onNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier.fillMaxWidth()) {
        destinations.forEach { destination ->
            val isSelected = currentDestination == destination
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(destination) },
                label = { DestinationTitle(destination = destination) },
                icon = { DestinationIcon(destination = destination, isSelected = isSelected) },
            )
        }
    }
}

@Composable
private fun DestinationTitle(destination: Any) {
    val title =
        when (destination) {
            is DashboardScreen -> stringResource(R.string.title_dashboard)
            is ReportMachineBroken -> stringResource(R.string.title_report_machine)
            is GymLocationsScreen -> stringResource(R.string.title_personal_trainers)
            else -> ""
        }
    Text(text = title)
}

@Composable
private fun DestinationIcon(destination: Any, isSelected: Boolean) {
    val painterResId =
        when (destination) {
            is DashboardScreen ->
                if (isSelected) {
                    R.drawable.ic_home_icon_filled
                } else {
                    R.drawable.ic_home_icon_outlined
                }
            is ReportMachineBroken ->
                if (isSelected) {
                    R.drawable.ic_gym_icon_filled
                } else {
                    R.drawable.ic_gym_icon_outlined
                }
            is GymLocationsScreen ->
                if (isSelected) {
                    R.drawable.ic_groups_icon_filled
                } else {
                    R.drawable.ic_groups_icon_outlined
                }
            else -> R.drawable.ic_home_icon_filled
        }

    Icon(painter = painterResource(id = painterResId), contentDescription = null)
}
