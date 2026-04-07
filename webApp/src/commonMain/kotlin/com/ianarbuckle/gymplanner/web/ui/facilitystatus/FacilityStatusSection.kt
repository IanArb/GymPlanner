package com.ianarbuckle.gymplanner.web.ui.facilitystatus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.web.generated.resources.Res
import com.ianarbuckle.gymplanner.web.generated.resources.ic_build
import com.ianarbuckle.gymplanner.web.generated.resources.ic_check_circle
import com.ianarbuckle.gymplanner.web.generated.resources.ic_chevron_right
import com.ianarbuckle.gymplanner.web.generated.resources.ic_warning
import org.jetbrains.compose.resources.painterResource

enum class EquipmentStatus {
    OUT_OF_ORDER,
    MAINTENANCE,
    OPERATIONAL,
}

data class EquipmentItem(
    val name: String,
    val detail: String,
    val location: String,
    val status: EquipmentStatus,
)

@Composable
fun FacilityStatusSection(
    items: List<EquipmentItem>,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FacilityStatusHeader(onViewAllClick = onViewAllClick)

        Card(
            modifier = Modifier.widthIn(max = 520.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            items.forEachIndexed { index, item ->
                EquipmentRow(item = item)
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun FacilityStatusHeader(onViewAllClick: () -> Unit) {
    Row(
        modifier = Modifier.widthIn(max = 520.dp).fillMaxWidth().padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Facility Status",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        TextButton(onClick = onViewAllClick) {
            Text(
                text = "VIEW ALL ASSETS",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0D0D0D),
                letterSpacing = 0.5.sp,
            )
        }
    }
}

@Composable
private fun EquipmentRow(item: EquipmentItem) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EquipmentIcon(status = item.status)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "${item.detail} • ${item.location}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        StatusBadge(status = item.status)

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(Res.drawable.ic_chevron_right),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun EquipmentIcon(status: EquipmentStatus) {
    val (backgroundColor, iconTint, icon) =
        when (status) {
            EquipmentStatus.OUT_OF_ORDER ->
                Triple(Color(0xFFFFE5E5), Color(0xFFE53935), Res.drawable.ic_warning)
            EquipmentStatus.MAINTENANCE ->
                Triple(Color(0xFFFFF3E0), Color(0xFFE8500A), Res.drawable.ic_build)
            EquipmentStatus.OPERATIONAL ->
                Triple(Color(0xFFF0F0F0), Color(0xFF757575), Res.drawable.ic_check_circle)
        }

    Box(
        modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
private fun StatusBadge(status: EquipmentStatus) {
    val (label, backgroundColor, textColor) =
        when (status) {
            EquipmentStatus.OUT_OF_ORDER -> Triple("OUT OF ORDER", Color(0xFFE53935), Color.White)
            EquipmentStatus.MAINTENANCE -> Triple("MAINTENANCE", Color(0xFFE8500A), Color.White)
            EquipmentStatus.OPERATIONAL -> Triple("OPERATIONAL", Color(0xFF43A047), Color.White)
        }

    Box(
        modifier =
            Modifier.clip(RoundedCornerShape(4.dp))
                .background(backgroundColor)
                .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
        )
    }
}
