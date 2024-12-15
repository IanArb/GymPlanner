package com.ianarbuckle.gymplanner.android.dashboard.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.fitnessclass.domain.Duration
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DashboardContent(
    innerPadding: PaddingValues,
    items: ImmutableList<FitnessClass>,
    modifier: Modifier = Modifier,
    onViewScheduleClick: () -> Unit,
    onBookPersonalTrainerClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues = innerPadding)
            .padding(16.dp),
    ) {
        item {
            GymClassesContent(
                classesCarouselItems = items,
                modifier = modifier,
                onViewScheduleClick = onViewScheduleClick
            )
        }

        item {
            BookPersonalTrainerCard(
                modifier = modifier,
                onBookPersonalTrainerClick = onBookPersonalTrainerClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashboardPreview() {
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
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Westwood Gym") })
            }
        ) { innerPadding ->
            DashboardContent(
                innerPadding = innerPadding,
                items = carouselItems,
                onViewScheduleClick = {

                },
                onBookPersonalTrainerClick = {

                }
            )
        }

    }
}

const val PERSONAL_TRAINER_BACKDROP_URL = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.libm.co.uk%2Fwp-content%2Fuploads%2F2017%2F09%2F35-Personal-Trainer-Fitness-Instructor-Course.jpg&f=1&nofb=1&ipt=4a7dd2591bf00e81d8ef2268a91853c3d8d7d4eee73c567d6230fd5a44711716&ipo=images"