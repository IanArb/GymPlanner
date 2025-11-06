package com.ianarbuckle.gymplanner.android.gymlocations.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun GymLocationsSelection(
    innerPadding: PaddingValues,
    gyms: ImmutableList<GymLocations>,
    onClick: (GymLocations) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(innerPadding)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.testTag(GymLocationsGridTag),
        ) {
            items(gyms) { gym ->
                GymLocationCard(
                    imageUrl = gym.imageUrl,
                    title = gym.title,
                    subTitle = gym.subTitle,
                    modifier = Modifier.clickable { onClick(gym) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewsCombined
@Composable
private fun GymLocationsSelectionPreview() {
    GymAppTheme {
        Scaffold(topBar = { TopAppBar(title = { Text("Westwood Gym") }) }) {
            GymLocationsSelection(
                innerPadding = it,
                gyms = DataProvider.gymLocations().toImmutableList(),
                onClick = {},
            )
        }
    }
}

const val GymLocationsGridTag = "GymLocationsGrid"
