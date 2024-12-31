package com.ianarbuckle.gymplanner.android.dashboard.presentation

import android.content.res.Configuration
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.displayTime
import com.ianarbuckle.gymplanner.android.utils.toLocalTime
import com.ianarbuckle.gymplanner.fitnessclass.domain.Duration
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DashboardContent(
    innerPadding: PaddingValues,
    items: ImmutableList<FitnessClass>,
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
                    classesCarouselItems = items,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(10.dp))

                BookPersonalTrainerCard(
                    onBookPersonalTrainerClick = onBookPersonalTrainerClick,
                )

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
fun BookPersonalTrainerCard(
    onBookPersonalTrainerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Book a personal trainer",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp,
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                AsyncImage(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    model = PERSONAL_TRAINER_BACKDROP_URL,
                    contentDescription = "Personal Trainer Backdrop",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_placeholder),
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Need help?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                    Text(text = "Book a 6-week personal training program today.")
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = onBookPersonalTrainerClick) {
                        Text("Book now!")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymClassesCarousel(
    classesCarouselItems: ImmutableList<FitnessClass>,
    modifier: Modifier = Modifier,
) {
    if (classesCarouselItems.isEmpty()) {
        Text(
            text = "No classes available at the moment.",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    } else {
        HorizontalUncontainedCarousel(
            state = rememberCarouselState {
                classesCarouselItems.count()
            },
            itemSpacing = 8.dp,
            itemWidth = 320.dp,
            modifier = modifier,
        ) { index ->
            val item = classesCarouselItems[index]

            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp,
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier.fillMaxHeight(),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    model = item.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_placeholder),
                )
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                    Text(text = item.description)

                    Spacer(modifier = Modifier.padding(10.dp))

                    val startDisplayTime = item.startTime.toLocalTime().displayTime()
                    val endDisplayTime = item.endTime.toLocalTime().displayTime()

                    val classTime = "$startDisplayTime - $endDisplayTime"

                    Row {
                        Icon(painterResource(id = R.drawable.ic_clock), contentDescription = null)
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(text = classTime)
                    }

                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardPreview() {
    val carouselItems = persistentListOf(
        FitnessClass(
            dayOfWeek = "MONDAY",
            name = "Pilates Class",
            description = "Come join our pilates class today!",
            imageUrl = "",
            startTime = "07:00:00",
            endTime = "08:00:00",
            duration = Duration(
                value = 3600,
                unit = "SECONDS",
            ),
        ),
        FitnessClass(
            dayOfWeek = "MONDAY",
            name = "Strength training",
            description = "We will be focusing on lower body today!",
            imageUrl = "",
            startTime = "07:00:00",
            endTime = "08:00:00",
            duration = Duration(
                value = 3600,
                unit = "SECONDS",
            ),
        ),
        FitnessClass(
            dayOfWeek = "MONDAY",
            name = "Body pump",
            description = "Come join us for body bump!",
            imageUrl = "",
            startTime = "07:00:00",
            endTime = "08:00:00",
            duration = Duration(
                value = 3600,
                unit = "SECONDS",
            ),
        ),
    )

    GymAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Westwood Gym") })
            },
        ) { innerPadding ->
            DashboardContent(
                innerPadding = innerPadding,
                items = carouselItems,
                onViewScheduleClick = {
                },
                onBookPersonalTrainerClick = {
                },
            )
        }
    }
}

@Suppress("MaxLineLength")
const val PERSONAL_TRAINER_BACKDROP_URL = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.libm.co.uk%2Fwp-content%2Fuploads%2F2017%2F09%2F35-Personal-Trainer-Fitness-Instructor-Course.jpg&f=1&nofb=1&ipt=4a7dd2591bf00e81d8ef2268a91853c3d8d7d4eee73c567d6230fd5a44711716&ipo=images"
