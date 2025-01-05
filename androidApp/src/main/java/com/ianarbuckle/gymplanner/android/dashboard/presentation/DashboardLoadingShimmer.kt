package com.ianarbuckle.gymplanner.android.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun DashboardLoadingShimmer(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Today's Classes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = "View Weekly Schedule",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Blue,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ClassesShimmer(shimmer = shimmer)

                Spacer(modifier = Modifier.height(10.dp))

                TrainerCardShimmer(shimmer = shimmer)
            }
        }
    }
}

@Composable
private fun TrainerCardShimmer(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .shimmer(shimmer),
    ) {
        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(4.dp),
                ),
        )
        Spacer(modifier = Modifier.height(6.dp))

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(WidthSizeLarge)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(4.dp),
                        ),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp),
                        ),
                )
            }
        }
    }
}

@Composable
private fun ClassesShimmer(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.shimmer(shimmer),
    ) {
        items(ItemsCount) {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp,
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .width(320.dp)
                    .height(300.dp),
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .height(24.dp)
                                .fillMaxWidth(WidthSizeMedium)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(4.dp),
                                ),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .height(16.dp)
                                .fillMaxWidth(WidthSizeLarge)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(4.dp),
                                ),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .height(16.dp)
                                .fillMaxWidth(WidthSizeSmall)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(4.dp),
                                ),
                        )
                    }
                }
            }
        }
    }
}

private const val ItemsCount = 3
private const val WidthSizeSmall = 0.4f
private const val WidthSizeMedium = 0.6f
private const val WidthSizeLarge = 0.8f
