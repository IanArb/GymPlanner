package com.ianarbuckle.gymplanner.android.commonUi

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController
import com.ianarbuckle.gymplanner.android.core.navigation.BottomNavigationItem
import com.ianarbuckle.gymplanner.android.core.navigation.DashboardScreen
import com.ianarbuckle.gymplanner.android.core.navigation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.core.navigation.ReportMachineBroken
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BottomNavigationBar(
    navController: NavController,
    navigationItems: ImmutableList<BottomNavigationItem>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    NavigationBar {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    when (index) {
                        0 -> {
                            navController.navigate(DashboardScreen) {
                                restoreState()
                            }
                        }
                        1 -> {
                            navController.navigate(ReportMachineBroken) {
                                restoreState()
                            }
                        }
                        2 -> {
                            navController.navigate(GymLocationsScreen) {
                                restoreState()
                            }
                        }
                    }
                    onItemSelected(index)
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

private fun NavOptionsBuilder.restoreState() {
    // Avoid multiple copies of the same destination when
    // reselecting the same item
    launchSingleTop = true
    // Restore state when reselecting a previously selected item
    restoreState = true
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    val navigationItems = persistentListOf(
        BottomNavigationItem(
            title = "Dashboard",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavigationItem(
            title = "Report Machine",
            selectedIcon = Icons.Filled.Build,
            unselectedIcon = Icons.Outlined.Build,
        ),
        BottomNavigationItem(
            title = "Personal Trainers",
            selectedIcon = Icons.Filled.Face,
            unselectedIcon = Icons.Outlined.Face,
        )
    )

    GymPlannerTheme {
        BottomNavigationBar(
            navController = rememberNavController(),
            navigationItems = navigationItems,
            selectedItemIndex = 0,
            onItemSelected = { }
        )
    }
}