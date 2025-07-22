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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun PersonalTrainersLoadingShimmer(
    innerPadding: PaddingValues,
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(innerPadding)) {
        LazyColumn { items(ItemsCount) { PersonalTrainerItemShimmer(shimmer = shimmer) } }
    }
}

@Composable
fun PersonalTrainerItemShimmer(shimmer: Shimmer, modifier: Modifier = Modifier) {
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
                            .shimmer(shimmer)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp),
                            )
                )

                Column(modifier = Modifier.padding(start = 16.dp)) {
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
                    Spacer(modifier = Modifier.padding(4.dp))
                    Box(
                        modifier =
                            Modifier.height(16.dp)
                                .fillMaxWidth(WidthSizeSmall)
                                .shimmer(shimmer)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(4.dp),
                                )
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
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

            Spacer(modifier = Modifier.padding(8.dp))

            Box(
                modifier =
                    Modifier.height(48.dp)
                        .fillMaxWidth()
                        .shimmer(shimmer)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(4.dp),
                        )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonalTrainersLoadingShimmerPreview() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    PersonalTrainersLoadingShimmer(innerPadding = PaddingValues(0.dp), shimmer = shimmer)
}

private const val ItemsCount = 6
private const val WidthSizeSmall = 0.4f
private const val WidthSizeMedium = 0.6f
private const val WidthSizeLarge = 0.8f
