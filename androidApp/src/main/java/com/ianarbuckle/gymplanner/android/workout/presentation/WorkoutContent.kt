package com.ianarbuckle.gymplanner.android.workout.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.android.core.utils.DataProvider
import com.ianarbuckle.gymplanner.android.theme.GymAppTheme
import com.ianarbuckle.gymplanner.model.Client

@Composable
fun WorkoutContent(
    contentPadding: PaddingValues,
    client: Client,
    modifier: Modifier = Modifier,
    enableViewAllButton: Boolean = true,
    onViewAllClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(contentPadding)
            .padding(16.dp),
    ) {
        Row {
            Text(
                client.gymPlan?.name ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.weight(1f)
            )
            if (enableViewAllButton) {
                Text(
                    "View Archive",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        onViewAllClick()
                    }
                )
            }
        }

        Spacer(modifier = modifier.padding(10.dp))

        client.gymPlan?.let { WorkoutPlanInfo(it, modifier) }

        Spacer(modifier = modifier.padding(10.dp))

        Text(
            "Workouts",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = modifier.padding(10.dp))

        val sessions = client.gymPlan?.sessions ?: emptyList()
        if (sessions.isEmpty()) {
            EmptyWorkout(personalTrainerName = client.gymPlan?.personalTrainer?.firstName ?: "")
        } else {
            WorkoutsCard(sessions) {

            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultPreview() {
    GymAppTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Gym Plan") }) }
        ) {
            WorkoutContent(
                it,
                DataProvider.client()
            ) {

            }
        }
    }
}