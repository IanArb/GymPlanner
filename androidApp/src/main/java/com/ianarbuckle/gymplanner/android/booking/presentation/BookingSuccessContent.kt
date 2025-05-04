package com.ianarbuckle.gymplanner.android.booking.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.ui.common.Avatar
import com.ianarbuckle.gymplanner.android.utils.AnimatedIcon
import com.ianarbuckle.gymplanner.android.utils.AnimatedIconState

@Composable
fun BookingConfirmationContent(
    trainerName: String,
    avatarUrl: String,
    sessionDate: String,
    sessionTime: String,
    location: String,
    goHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        AnimatedIcon(
            state = AnimatedIconState(),
        )

        Text(
            text = "Session Confirmed!",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            InfoRow(Icons.Outlined.DateRange, sessionDate)
            InfoRow(ImageVector.vectorResource(id = R.drawable.ic_clock), sessionTime)
            InfoRow(Icons.Outlined.Place, location)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Avatar(
                    imageUrl = avatarUrl,
                    modifier = Modifier.size(48.dp),
                )
                Column {
                    Text(trainerName, style = MaterialTheme.typography.titleMedium)
                    Text("Personal Trainer", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Button(
                onClick = goHomeClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Go to Home")
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookingConfirmationContentPreview() {
    BookingConfirmationContent(
        trainerName = "John Doe",
        sessionDate = "Thursday, 12 Feburary 2025",
        sessionTime = "10:00 am",
        location = "Clontarf",
        avatarUrl = "",
        goHomeClick = {},
    )
}
