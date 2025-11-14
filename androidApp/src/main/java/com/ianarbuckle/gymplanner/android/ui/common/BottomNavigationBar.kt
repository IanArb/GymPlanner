package com.ianarbuckle.gymplanner.android.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ianarbuckle.gymplanner.android.navigation.BottomNavigationItem
import kotlinx.collections.immutable.PersistentList

@Composable
fun BottomNavigationBar(
    navigationItems: PersistentList<BottomNavigationItem>,
    selectItemIndex: Int,
    onItemSelect: (Int) -> Unit,
    onNavigateTo: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier.fillMaxWidth()) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectItemIndex == index,
                onClick = {
                    onItemSelect(index)
                    onNavigateTo(index)
                },
                label = { Text(text = item.title) },
                icon = {
                    if (selectItemIndex == index) {
                        Icon(
                            painter = item.selectedIcon.asPainter(),
                            contentDescription = item.title,
                        )
                    } else {
                        Icon(
                            painter = item.unselectedIcon.asPainter(),
                            contentDescription = item.title,
                        )
                    }
                },
            )
        }
    }
}
