package com.ianarbuckle.gymplanner.web.ui.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.web.generated.resources.Res
import com.ianarbuckle.gymplanner.web.generated.resources.ic_bar_chart
import com.ianarbuckle.gymplanner.web.generated.resources.ic_build
import com.ianarbuckle.gymplanner.web.generated.resources.ic_dashboard
import com.ianarbuckle.gymplanner.web.generated.resources.ic_group
import com.ianarbuckle.gymplanner.web.generated.resources.ic_logout
import com.ianarbuckle.gymplanner.web.generated.resources.ic_settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val BrandBlack = Color(0xFF0D0D0D)

enum class NavDestination {
    DASHBOARD,
    SCHEDULE,
    EQUIPMENT,
    TRAINERS,
    REPORTS,
}

data class NavItem(val label: String, val destination: NavDestination, val icon: DrawableResource)

@Composable
fun SidebarNavigation(
    selectedDestination: NavDestination,
    onDestinationSelected: (NavDestination) -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navItems =
        listOf(
            NavItem("DASHBOARD", NavDestination.DASHBOARD, Res.drawable.ic_dashboard),
            NavItem("EQUIPMENT", NavDestination.EQUIPMENT, Res.drawable.ic_build),
            NavItem("TRAINERS", NavDestination.TRAINERS, Res.drawable.ic_group),
            NavItem("REPORTS", NavDestination.REPORTS, Res.drawable.ic_bar_chart),
        )

    Column(
        modifier =
            modifier
                .width(180.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 24.dp, horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            navItems.forEach { item ->
                NavItemRow(
                    item = item,
                    isSelected = item.destination == selectedDestination,
                    onClick = { onDestinationSelected(item.destination) },
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.height(16.dp))

        BottomNavItem(
            label = "SETTINGS",
            icon = Res.drawable.ic_settings,
            onClick = onSettingsClick,
        )

        Spacer(modifier = Modifier.height(4.dp))

        BottomNavItem(label = "LOGOUT", icon = Res.drawable.ic_logout, onClick = onLogoutClick)
    }
}

@Composable
private fun NavItemRow(item: NavItem, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) BrandBlack else Color.Transparent
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier =
            Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(backgroundColor)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(item.icon),
            contentDescription = item.label,
            tint = contentColor,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = item.label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = contentColor,
            letterSpacing = 0.5.sp,
        )
    }
}

@Composable
private fun BottomNavItem(label: String, icon: DrawableResource, onClick: () -> Unit) {
    Row(
        modifier =
            Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp,
        )
    }
}
