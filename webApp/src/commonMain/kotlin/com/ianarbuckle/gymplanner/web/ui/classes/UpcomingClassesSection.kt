package com.ianarbuckle.gymplanner.web.ui.classes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ianarbuckle.gymplanner.web.generated.resources.Res
import com.ianarbuckle.gymplanner.web.generated.resources.ic_schedule
import org.jetbrains.compose.resources.painterResource

enum class ClassFilter {
    ALL
}

data class FitnessClassItem(
    val name: String,
    val description: String,
    val timeSlot: String,
    val imageUrl: String,
)

@Composable
fun UpcomingClassesSection(
    classes: List<FitnessClassItem>,
    isLoading: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier,
) {
    var selectedFilter by remember { mutableStateOf(ClassFilter.ALL) }

    Column(modifier = modifier) {
        UpcomingClassesHeader(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading ->
                Box(
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            errorMessage != null ->
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                    )
                }
            classes.isEmpty() ->
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No classes scheduled today",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                    )
                }
            else ->
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
            Modifier.fillMaxWidth()
                .height(180.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (item.imageUrl.isNotBlank()) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

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
private fun ClassDetails(item: FitnessClassItem, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
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

        if (item.description.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Normal,
                maxLines = 3,
            )
        }
    }
}
