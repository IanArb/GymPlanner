package com.ianarbuckle.gymplanner.android.gymlocations.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun GymLocationsLoadingShimmer(
    innerPadding: PaddingValues,
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(innerPadding)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier,
        ) {
            items(ItemsCount) { // Number of shimmer cards to display
                GymLocationCardLoadingShimmer(shimmer = shimmer)
            }
        }
    }
}

@Composable
fun GymLocationCardLoadingShimmer(shimmer: Shimmer, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column {
            Box(
                modifier =
                    Modifier.height(120.dp)
                        .fillMaxWidth()
                        .shimmer(shimmer)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(16.dp),
                        )
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier =
                        Modifier.height(20.dp)
                            .fillMaxWidth(WidthSizeMedium)
                            .shimmer(shimmer)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp),
                            )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier =
                        Modifier.height(16.dp)
                            .fillMaxWidth(WidthSizeLarge)
                            .shimmer(shimmer)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp),
                            )
                )
            }
        }
    }
}

private const val ItemsCount = 6
private const val WidthSizeMedium = 0.6f
private const val WidthSizeLarge = 0.8f
