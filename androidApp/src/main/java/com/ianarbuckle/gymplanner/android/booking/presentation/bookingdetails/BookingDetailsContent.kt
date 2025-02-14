package com.ianarbuckle.gymplanner.android.booking.presentation.bookingdetails

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.ui.common.Avatar
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.displayFormattedDate
import com.ianarbuckle.gymplanner.android.utils.parseToLocalDate

@Composable
fun BookingDetailsContent(
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    selectedDate: String,
    selectedTimeSlot: String,
    personalTrainerName: String,
    personalTrainerAvatarUrl: String,
    location: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Column {
            Text(
                text = "Booking Details",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                modifier = Modifier.padding(start = 16.dp),
            )

            BookingDetailCard(
                selectedDate = selectedDate,
                selectedTimeSlot = selectedTimeSlot,
                location = location,
            )

            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp,
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Row {
                    Avatar(
                        imageUrl = personalTrainerAvatarUrl,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(16.dp),
                    )

                    Column {
                        Text(
                            text = personalTrainerName,
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                            modifier = Modifier.padding(
                                top = 16.dp,
                                start = 8.dp,
                            ),
                        )

                        Text(
                            text = "Personal Trainer",
                            fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                            modifier = Modifier.padding(
                                top = 4.dp,
                                start = 8.dp,
                            ),
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                Button(
                    onClick = { onConfirmClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                ) {
                    Text(text = "Confirm Booking")
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Button(
                    onClick = { onCancelClick() },
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.error,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}

@Composable
fun BookingDetailCard(
    selectedDate: String,
    selectedTimeSlot: String,
    location: String,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Spacer(modifier = Modifier.padding(8.dp))

            val date = selectedDate.parseToLocalDate().displayFormattedDate()
            BookingDetailSlot(
                icon = Icons.Filled.DateRange,
                title = "Date",
                value = date,
            )

            Spacer(modifier = Modifier.padding(8.dp))

            BookingDetailSlot(
                icon = ImageVector.vectorResource(id = R.drawable.ic_clock),
                title = "Time",
                value = selectedTimeSlot,
            )

            Spacer(modifier = Modifier.padding(8.dp))

            val gymLocation = resolveGymLocation(location)

            BookingDetailSlot(
                icon = ImageVector.vectorResource(id = R.drawable.ic_location),
                title = "Location",
                value = gymLocation,
            )
        }
    }
}

private fun resolveGymLocation(location: String): String {
    val gymLocation = when (location) {
        CLONTARF -> {
            "Clontarf"
        }

        ASTONQUAY -> {
            "Aston Quay"
        }

        LEOPARDSTOWN -> {
            "Leopardstown"
        }

        WESTMANSTOWN -> {
            "Westmanstown"
        }

        BLANCHARDSTOWN -> {
            "Blanchardstown"
        }

        DUNLOAGHAIRE -> {
            "Dun Laoghaire"
        }

        SANDYMOUNT -> {
            "Sandymount"
        }

        else -> {
            "Clontarf"
        }
    }
    return gymLocation
}

@Composable
fun BookingDetailSlot(
    title: String,
    icon: ImageVector,
    value: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Row(
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.padding(end = 8.dp),
        )

        Text(
            text = "$title: $value",
            modifier = Modifier.padding(bottom = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun BookingConfirmationContentPreview() {
    GymAppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            BookingDetailsContent(
                selectedDate = "2025-02-13",
                selectedTimeSlot = "10:00 am",
                personalTrainerName = "John Doe",
                personalTrainerAvatarUrl = "https://example.com/avatar.jpg",
                location = "Clontarf",
                onConfirmClick = {
                },
                onCancelClick = {
                },
            )
        }
    }
}

private const val CLONTARF = "CLONTARF"
private const val ASTONQUAY = "ASTONQUAY"
private const val LEOPARDSTOWN = "LEOPARDSTOWN"
private const val WESTMANSTOWN = "WESTMANSTOWN"
private const val BLANCHARDSTOWN = "BLANCHARDSTOWN"
private const val DUNLOAGHAIRE = "DUNLOAGHAIRE"
private const val SANDYMOUNT = "SANDYMOUNT"
