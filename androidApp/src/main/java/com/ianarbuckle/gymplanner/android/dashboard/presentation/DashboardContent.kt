package com.ianarbuckle.gymplanner.android.dashboard.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.core.presentation.GymPlannerTheme
import com.ianarbuckle.gymplanner.android.core.utils.displayTime
import com.ianarbuckle.gymplanner.android.core.utils.toLocalTime
import com.ianarbuckle.gymplanner.model.Duration
import com.ianarbuckle.gymplanner.model.FitnessClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DashboardContent(
    innerPadding: PaddingValues,
    classesCarouselItems: ImmutableList<FitnessClass>,
    modifier: Modifier = Modifier,
    onViewScheduleClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues = innerPadding)
            .padding(16.dp),
    ) {
        item {
            Row {
                Text(
                    "Today's Classes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.weight(1f)
                )
                Text(
                    "View Weekly Schedule",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        onViewScheduleClick()
                    }
                )
            }

            Spacer(modifier = modifier.padding(10.dp))

            GymClassesContent(
                classesCarouselItems = classesCarouselItems,
                modifier = modifier,
            )
        }

        item {
            Spacer(modifier = modifier.padding(10.dp))

            Row {
                Text(
                    "Book a personal trainer",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.weight(1f)
                )
            }

            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                modifier = modifier
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    modifier = Modifier.height(200.dp),
                    model = PERSONAL_TRAINER_BACKSHOT_URL,
                    contentDescription = null,
                )
                Column(modifier = modifier.padding(16.dp)) {
                    Text(
                        text = "Need help?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                    Text(text = "Book a 6 week personal training program today")
                    Spacer(modifier = modifier.padding(10.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text("Book now!")
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GymClassesContent(
    classesCarouselItems: ImmutableList<FitnessClass>,
    modifier: Modifier = Modifier,
) {
    HorizontalUncontainedCarousel(
        state = rememberCarouselState {
            classesCarouselItems.count()
        },
        itemSpacing = 8.dp,
        itemWidth = 320.dp
    ) { index ->
        val item = classesCarouselItems[index]

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = modifier
                .fillMaxWidth()
        ) {

            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                model = item.imageUrl,
                contentDescription = null,
            )
            Column(
                modifier = modifier.padding(16.dp)
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                Text(text = item.description)

                Spacer(modifier = modifier.padding(10.dp))

                val startDisplayTime = item.startTime.toLocalTime().displayTime()
                val endDisplayTime = item.endTime.toLocalTime().displayTime()

                val classTime = "$startDisplayTime - $endDisplayTime"

                Row {
                    Icon(painterResource(id = R.drawable.ic_clock), contentDescription = null)
                    Spacer(modifier = modifier.padding(4.dp))
                    Text(text = classTime)
                }

                Spacer(modifier = modifier.padding(4.dp))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DashboardPreview() {
    val carouselItems = persistentListOf(
        FitnessClass(
            dayOfWeek = "MONDAY",
            name = "Pilates Class",
            description = "Come join our pilates class today!",
            imageUrl = "",
            startTime = "",
            endTime = "",
            duration = Duration(
                value = 0,
                unit = "SECONDS"
            )
        ),
        FitnessClass(
            dayOfWeek = "MONDAY",
            name = "Strength training",
            description = "We will be focusing on lower body today!",
            imageUrl = "",
            startTime = "",
            endTime = "",
            duration = Duration(
                value = 0,
                unit = "SECONDS"
            )
        ),
        FitnessClass(
            dayOfWeek = "MONDAY",
            name = "Body pump",
            description = "Come join us for body bump!",
            imageUrl = "",
            startTime = "",
            endTime = "",
            duration = Duration(
                value = 0,
                unit = "SECONDS"
            )
        )
    )

    GymPlannerTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Westwood Gym") })
            }
        ) { innerPadding ->
            DashboardContent(
                innerPadding = innerPadding,
                classesCarouselItems = carouselItems,
                onViewScheduleClick = {

                }
            )
        }

    }
}

const val PERSONAL_TRAINER_BACKSHOT_URL = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.libm.co.uk%2Fwp-content%2Fuploads%2F2017%2F09%2F35-Personal-Trainer-Fitness-Instructor-Course.jpg&f=1&nofb=1&ipt=4a7dd2591bf00e81d8ef2268a91853c3d8d7d4eee73c567d6230fd5a44711716&ipo=images"