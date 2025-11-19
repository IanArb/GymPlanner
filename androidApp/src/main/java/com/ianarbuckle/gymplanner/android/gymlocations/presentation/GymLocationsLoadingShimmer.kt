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
import com.ianarbuckle.gymplanner.android.utils.shimmerEffect

@Composable
fun GymLocationsLoadingShimmer(innerPadding: PaddingValues, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(innerPadding)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier,
        ) {
            items(ItemsCount) { GymLocationCardLoadingShimmer() }
        }
    }
}

@Composable
fun GymLocationCardLoadingShimmer(modifier: Modifier = Modifier) {
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
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(16.dp),
                        )
                        .shimmerEffect()
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier =
                        Modifier.height(20.dp)
                            .fillMaxWidth(WidthSizeMedium)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp),
                            )
                            .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier =
                        Modifier.height(16.dp)
                            .fillMaxWidth(WidthSizeLarge)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp),
                            )
                            .shimmerEffect()
                )
            }
        }
    }
}

private const val ItemsCount = 6
private const val WidthSizeMedium = 0.6f
private const val WidthSizeLarge = 0.8f
