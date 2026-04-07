package com.ianarbuckle.gymplanner.web.ui.trainers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.ianarbuckle.gymplanner.web.generated.resources.ic_chat
import com.ianarbuckle.gymplanner.web.generated.resources.ic_chevron_right
import com.ianarbuckle.gymplanner.web.generated.resources.ic_schedule
import org.jetbrains.compose.resources.painterResource

enum class TrainerAvailability {
    AVAILABLE,
    IN_SESSION,
}

data class TrainerItem(
    val name: String,
    val availability: TrainerAvailability,
    val sessionTimeLeft: String? = null,
)

@Composable
fun TodaysTeamSection(
    trainers: List<TrainerItem>,
    onTrainerClick: (TrainerItem) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TodaysTeamHeader()

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            trainers.forEach { trainer ->
                TrainerCard(trainer = trainer, onClick = { onTrainerClick(trainer) })
            }
        }
    }
}

@Composable
private fun TodaysTeamHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Today's Team",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun TrainerCard(trainer: TrainerItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TrainerAvatar(availability = trainer.availability)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trainer.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(2.dp))
                when (trainer.availability) {
                    TrainerAvailability.AVAILABLE -> AvailableLabel()
                    TrainerAvailability.IN_SESSION -> InSessionLabel()
                }
            }

            Icon(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = "View trainer details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun TrainerAvatar(availability: TrainerAvailability) {
    val statusColor = when (availability) {
        TrainerAvailability.AVAILABLE -> Color.Green
        TrainerAvailability.IN_SESSION -> Color.Red
    }

    Box {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "PT",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(Color.White)
                .align(Alignment.BottomEnd)
                .offset(x = 2.dp, y = 2.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(statusColor)
                    .align(Alignment.Center),
            )
        }
    }
}

@Composable
private fun AvailableLabel() {
    Text(
        text = "AVAILABLE NOW",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0D0D0D),
        letterSpacing = 0.5.sp,
    )
}

@Composable
private fun InSessionLabel() {
    Column {
        Text(
            text = "UNAVAILABLE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp,
        )
    }
}
