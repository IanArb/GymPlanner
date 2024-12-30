package com.ianarbuckle.gymplanner.android.workout.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.dayOfWeekDisplayName
import com.ianarbuckle.gymplanner.android.utils.differenceInDays
import com.ianarbuckle.gymplanner.android.utils.monthDisplayName
import com.ianarbuckle.gymplanner.android.utils.parseToLocalDateTime
import com.ianarbuckle.gymplanner.clients.domain.GymPlan
import java.time.format.TextStyle

@Composable
fun WorkoutPlanInfo(
    gymPlan: GymPlan,
    modifier: Modifier = Modifier,
) {
    val startTime = parseToLocalDateTime(gymPlan.startDate)
    val endTime = parseToLocalDateTime(gymPlan.endDate)

    val daysDifference = differenceInDays(
        startDate = startTime.date,
        endDate = endTime.date,
    )

    val startDayOfWeek = startTime.dayOfWeekDisplayName(TextStyle.SHORT)
    val startMonth = startTime.monthDisplayName(TextStyle.FULL)

    val endDayOfWeek =
        endTime.dayOfWeekDisplayName(TextStyle.SHORT)
    val endMonth = endTime.monthDisplayName(TextStyle.FULL)

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                "Trainer - ${gymPlan.personalTrainer.firstName}",
                fontSize = 14.sp,
            )
            val start = "$startDayOfWeek ${startTime.dayOfMonth} $startMonth"
            val end = "$endDayOfWeek ${endTime.dayOfMonth} $endMonth"
            Text(
                "Period - $start to $end",
                fontSize = 14.sp,
            )
            Text(
                "Days - $daysDifference",
                fontSize = 14.sp,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun GymPlanInfoCardPreview() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gym Plan") })
        },
    ) { padding ->
        WorkoutPlanInfo(gymPlan = DataProvider.gymPlan(), Modifier.padding(padding))
    }
}
