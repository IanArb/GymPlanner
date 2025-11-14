package com.ianarbuckle.gymplanner.android.navigation

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: IconSource,
    val unselectedIcon: IconSource,
    val badgeCount: Int? = null,
)
