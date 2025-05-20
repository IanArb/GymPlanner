package com.ianarbuckle.gymplanner.android.dashboard.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.ui.common.Avatar
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.displayShortFormattedDate
import com.ianarbuckle.gymplanner.android.utils.displayTime
import com.ianarbuckle.gymplanner.android.utils.parseToLocalDate
import com.ianarbuckle.gymplanner.android.utils.toLocalTime
import com.ianarbuckle.gymplanner.android.utils.toLocalisedString
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsCarousel(
    bookings: ImmutableList<BookingResponse>,
    modifier: Modifier = Modifier,
) {
    HorizontalUncontainedCarousel(
        state = rememberCarouselState {
            bookings.count()
        },
        itemSpacing = 8.dp,
        itemWidth = 320.dp,
        modifier = modifier,
    ) { index ->
        val item = bookings[index]

        BookingCard(
            personalTrainerName = item.personalTrainer.name,
            personalTrainerImageUrl = item.personalTrainer.imageUrl,
            date = item.bookingDate.parseToLocalDate().displayShortFormattedDate(),
            time = item.startTime.toLocalTime().displayTime(),
            location = item.personalTrainer.gymLocation.toLocalisedString(),
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Composable
fun BookingCard(
    personalTrainerName: String,
    personalTrainerImageUrl: String,
    date: String,
    time: String,
    location: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp),
                )

                Text(
                    text = date,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Spacer(Modifier.height(8.dp))

            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp),
                )

                Text(
                    text = time,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Spacer(Modifier.height(8.dp))

            Row {
                Avatar(
                    imageUrl = personalTrainerImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp),
                )

                Column {
                    Text(
                        text = personalTrainerName,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    )

                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BookingsCarouselPreview() {
    GymAppTheme {
        Surface {
            BookingsCarousel(
                bookings = DataProvider.bookings(),
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
