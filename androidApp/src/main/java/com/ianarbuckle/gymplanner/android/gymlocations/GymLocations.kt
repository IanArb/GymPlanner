package com.ianarbuckle.gymplanner.android.gymlocations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


@Composable
fun GymLocationsSelection(
    innerPadding: PaddingValues,
    gyms: ImmutableList<GymLocations>,
    modifier: Modifier = Modifier,
    onClick: (GymLocations) -> Unit,
) {
    Column(modifier = modifier.padding(innerPadding)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(gyms) { gym ->
                GymLocationCard(
                    imageUrl = gym.imageUrl,
                    title = gym.title,
                    subTitle = gym.subTitle,
                    modifier = modifier.clickable {
                        onClick(gym)
                    }
                )
            }
        }
    }
}

@Composable
fun GymLocationCard(
    imageUrl: String,
    title: String,
    subTitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = subTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GymLocationsSelectionPreview() {
    GymAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Westwood Gym") })
            }
        ) {
            GymLocationsSelection(
                innerPadding = it,
                gyms = DataProvider.gymLocations().toImmutableList()
            ) {

            }
        }
    }

}