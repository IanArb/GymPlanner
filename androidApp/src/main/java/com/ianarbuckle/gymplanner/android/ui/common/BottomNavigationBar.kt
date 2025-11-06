package com.ianarbuckle.gymplanner.android.ui.common

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
import androidx.compose.ui.Modifier
import com.ianarbuckle.gymplanner.android.navigation.BottomNavigationItem
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BottomNavigationBar(
    navigationItems: ImmutableList<BottomNavigationItem>,
    selectItemIndex: Int,
    onItemSelect: (Int) -> Unit,
    onNavigateTo: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                modifier = modifier,
                selected = selectItemIndex == index,
                onClick = {
                    when (index) {
                        0 -> {
                            onNavigateTo(0)
                        }
                        1 -> {
                            onNavigateTo(1)
                        }
                        2 -> {
                            onNavigateTo(2)
                        }
                    }
                    onItemSelect(index)
                },
                label = { Text(item.title) },
                icon = {
                    Icon(
                        imageVector =
                            if (index == selectItemIndex) {
                                item.selectedIcon
                            } else {
                                item.unselectedIcon
                            },
                        contentDescription = item.title,
                    )
                },
            )
        }
    }
}

@PreviewsCombined
@Composable
private fun BottomNavigationBarPreview() {
    val navigationItems =
        persistentListOf(
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
            ),
        )

    GymAppTheme {
        BottomNavigationBar(
            navigationItems = navigationItems,
            selectItemIndex = 0,
            onItemSelect = {},
            onNavigateTo = {},
        )
    }
}
