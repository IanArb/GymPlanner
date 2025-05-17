package com.ianarbuckle.gymplanner.android.dashboard.presentation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DashboardContent(
    innerPadding: PaddingValues,
    classes: ImmutableList<FitnessClass>,
    bookings: ImmutableList<BookingResponse>,
    onViewScheduleClick: () -> Unit,
    onBookPersonalTrainerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
    ) {
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Today's Classes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = "View Weekly Schedule",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Blue,
                        modifier = Modifier.clickable(onClick = onViewScheduleClick),
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                GymClassesCarousel(
                    classesCarouselItems = classes,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (bookings.isEmpty()) {
                    BookPersonalTrainerCard(
                        onBookPersonalTrainerClick = onBookPersonalTrainerClick,
                    )
                } else {
                    Text(
                        text = "Your Bookings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    BookingsCarousel(
                        bookings = bookings,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardPreview() {
    GymAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Westwood Gym") })
            },
        ) { innerPadding ->
            DashboardContent(
                innerPadding = innerPadding,
                classes = DataProvider.fitnessClasses(),
                onViewScheduleClick = {
                },
                onBookPersonalTrainerClick = {
                },
                bookings = DataProvider.bookings(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardPreviewEmptyBookings() {
    GymAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Westwood Gym") })
            },
        ) { innerPadding ->
            DashboardContent(
                innerPadding = innerPadding,
                classes = DataProvider.fitnessClasses(),
                onViewScheduleClick = {
                },
                onBookPersonalTrainerClick = {
                },
                bookings = persistentListOf(),
            )
        }
    }
}

@Suppress("MaxLineLength")
const val PERSONAL_TRAINER_BACKDROP_URL = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.libm.co.uk%2Fwp-content%2Fuploads%2F2017%2F09%2F35-Personal-Trainer-Fitness-Instructor-Course.jpg&f=1&nofb=1&ipt=4a7dd2591bf00e81d8ef2268a91853c3d8d7d4eee73c567d6230fd5a44711716&ipo=images"
