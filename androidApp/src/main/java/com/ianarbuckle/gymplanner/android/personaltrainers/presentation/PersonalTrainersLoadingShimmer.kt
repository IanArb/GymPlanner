package com.ianarbuckle.gymplanner.android.personaltrainers.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined
import com.ianarbuckle.gymplanner.android.utils.shimmerEffect

@Composable
fun PersonalTrainersLoadingShimmer(innerPadding: PaddingValues, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(innerPadding)) {
        LazyColumn { items(ItemsCount) { PersonalTrainerItemShimmer() } }
    }
}

@Composable
fun PersonalTrainerItemShimmer(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(16.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Box(
                    modifier =
                        Modifier.size(64.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp),
                            )
                            .shimmerEffect()
                )

                Column(modifier = Modifier.padding(start = 16.dp)) {
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
                    Spacer(modifier = Modifier.padding(4.dp))
                    Box(
                        modifier =
                            Modifier.height(16.dp)
                                .fillMaxWidth(WidthSizeSmall)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(4.dp),
                                )
                                .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
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

            Spacer(modifier = Modifier.padding(8.dp))

            Box(
                modifier =
                    Modifier.height(48.dp)
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(4.dp),
                        )
                        .shimmerEffect()
            )
        }
    }
}

@PreviewsCombined
@Composable
private fun PersonalTrainersLoadingShimmerPreview() {
    PersonalTrainersLoadingShimmer(innerPadding = PaddingValues(0.dp))
}

private const val ItemsCount = 6
private const val WidthSizeSmall = 0.4f
private const val WidthSizeMedium = 0.6f
private const val WidthSizeLarge = 0.8f
