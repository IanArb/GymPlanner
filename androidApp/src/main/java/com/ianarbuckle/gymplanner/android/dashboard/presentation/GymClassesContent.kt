package com.ianarbuckle.gymplanner.android.dashboard.presentation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun GymClassesContent(
    classesCarouselItems: ImmutableList<FitnessClass>,
    modifier: Modifier = Modifier,
    onViewScheduleClick: () -> Unit,
) {
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

    GymClassesCarousel(
        classesCarouselItems = classesCarouselItems,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymClassesCarousel(
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
                defaultElevation = 8.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = modifier.fillMaxHeight()
        ) {

            AsyncImage(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                model = item.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_placeholder)
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


@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GymClassesCarouselPreview() {
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
                unit = "SECONDS"
            )
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
                unit = "SECONDS"
            )
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
                unit = "SECONDS"
            )
        )
    )

    GymAppTheme {
        Surface(
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
        ) {
            GymClassesCarousel(
                classesCarouselItems = carouselItems
            )
        }
    }
}

