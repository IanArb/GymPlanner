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
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.clients.domain.Session

@Composable
fun WorkoutsCard(
    sessions: List<Session>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(sessions) { session ->
            Card(
                onClick = { onClick() },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp,
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row {
                    Text(
                        text = session.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, top = 16.dp),
                    )

                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Add",
                        modifier = Modifier.padding(end = 8.dp, top = 16.dp),
                    )
                }

                Spacer(modifier = Modifier.padding(4.dp))

                session.workout.map { workout ->
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = "${workout.sets} x ${workout.name}",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(4.dp))
            }
            Spacer(modifier = Modifier.padding(6.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun GymPlanWorkoutsCardPreview() {
    GymAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Gym Plan") })
            },
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                WorkoutsCard(
                    sessions = DataProvider.sessions(),
                    onClick = {
                    },
                )
            }
        }
    }
}
