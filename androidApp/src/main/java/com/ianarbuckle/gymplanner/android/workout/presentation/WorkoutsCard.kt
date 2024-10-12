package com.ianarbuckle.gymplanner.android.workout.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.android.core.presentation.GymPlannerTheme
import com.ianarbuckle.gymplanner.android.core.utils.DataProvider
import com.ianarbuckle.gymplanner.model.Session

@Composable
fun WorkoutsCard(
    sessions: List<Session>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    LazyColumn {
        items(sessions) { session ->
            Card(
                onClick = { onClick() },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Row {
                    Text(
                        text = session.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier
                            .weight(1f)
                            .padding(start = 16.dp, top = 16.dp),
                    )

                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Add",
                        modifier = modifier.padding(end = 8.dp, top = 16.dp)
                    )
                }

                Spacer(modifier = modifier.padding(4.dp))

                session.workout.map { workout ->
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = "${workout.sets} x ${workout.name}",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Spacer(modifier = modifier.padding(4.dp))

            }
            Spacer(modifier = modifier.padding(6.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GymPlanWorkoutsCardPreview() {
    GymPlannerTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Gym Plan") })
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                WorkoutsCard(sessions = DataProvider.sessions()) {
                }
            }
        }
    }
}