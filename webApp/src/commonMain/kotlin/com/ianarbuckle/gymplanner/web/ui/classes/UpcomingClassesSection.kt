package com.ianarbuckle.gymplanner.web.ui.classes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.web.generated.resources.Res
import com.ianarbuckle.gymplanner.web.generated.resources.ic_schedule
import org.jetbrains.compose.resources.painterResource

enum class ClassCategory {
    PEAK_HOUR,
    RECOVERY,
    STRENGTH,
}

enum class ClassFilter {
    ALL
}

data class FitnessClassItem(
    val name: String,
    val category: ClassCategory,
    val timeSlot: String,
    val coachName: String,
)

@Composable
fun UpcomingClassesSection(classes: List<FitnessClassItem>, modifier: Modifier = Modifier) {
    var selectedFilter by remember { mutableStateOf(ClassFilter.ALL) }

    Column(modifier = modifier) {
        UpcomingClassesHeader(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            classes.forEach { fitnessClass ->
                ClassCard(item = fitnessClass, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun UpcomingClassesHeader(
    selectedFilter: ClassFilter,
    onFilterSelected: (ClassFilter) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = "Today's Classes",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterButton(
                label = "VIEW ALL",
                isSelected = selectedFilter == ClassFilter.ALL,
                onClick = { onFilterSelected(ClassFilter.ALL) },
            )
        }
    }
}

@Composable
private fun FilterButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor =
                    if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                contentColor =
                    if (isSelected) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.onSurface,
            ),
    ) {
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun ClassCard(item: FitnessClassItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column {
            ClassImageCard(item = item)

            Spacer(modifier = Modifier.height(12.dp))

            ClassDetails(
                item = item,
                modifier = Modifier.padding(horizontal = 12.dp).padding(bottom = 12.dp),
            )
        }
    }
}

@Composable
private fun ClassImageCard(item: FitnessClassItem) {
    Box(
        modifier =
            Modifier.fillMaxWidth().height(180.dp).background(classCardGradient(item.category))
    ) {
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
        )

        CategoryBadge(
            category = item.category,
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 12.dp, bottom = 36.dp),
        )

        Text(
            text = item.name,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 12.dp, bottom = 12.dp),
        )
    }
}

@Composable
private fun CategoryBadge(category: ClassCategory, modifier: Modifier = Modifier) {
    val (label, color) =
        when (category) {
            ClassCategory.PEAK_HOUR -> "PEAK HOUR" to Color(0xFF0D0D0D)
            ClassCategory.RECOVERY -> "RECOVERY" to Color(0xFF424242)
            ClassCategory.STRENGTH -> "STRENGTH" to Color(0xFF9E9E9E)
        }

    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(4.dp))
                .background(color)
                .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
        )
    }
}

@Composable
private fun ClassDetails(item: FitnessClassItem, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.ic_schedule),
                contentDescription = null,
                tint = Color(0xFF0D0D0D),
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = item.timeSlot,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "COACH",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp,
            )
            Text(
                text = item.coachName,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

private fun classCardGradient(category: ClassCategory): Brush =
    when (category) {
        ClassCategory.PEAK_HOUR ->
            Brush.linearGradient(colors = listOf(Color(0xFF1A237E), Color(0xFF0D47A1)))
        ClassCategory.RECOVERY ->
            Brush.linearGradient(colors = listOf(Color(0xFF1B5E20), Color(0xFF2E7D32)))
        ClassCategory.STRENGTH ->
            Brush.linearGradient(colors = listOf(Color(0xFF4A148C), Color(0xFF6A1B9A)))
    }
