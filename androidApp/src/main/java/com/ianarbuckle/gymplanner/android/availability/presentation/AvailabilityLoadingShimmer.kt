package com.ianarbuckle.gymplanner.android.availability.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun AvailabilityLoadingShimmer(
  paddingValues: PaddingValues,
  shimmer: Shimmer,
  modifier: Modifier = Modifier,
) {
  Column(modifier.padding(paddingValues)) {
    PersonalTrainerCardShimmer(shimmer = shimmer)

    CalendarPickerCardShimmer(shimmer = shimmer)
  }
}

@Composable
fun PersonalTrainerCardShimmer(shimmer: Shimmer, modifier: Modifier = Modifier) {
  Column(modifier = modifier) {
    Card(
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier.padding(16.dp),
    ) {
      Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Box(
            modifier =
              Modifier.size(100.dp)
                .shimmer(shimmer)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
          )

          Spacer(modifier = Modifier.padding(2.dp))

          Box(
            modifier =
              Modifier.height(20.dp)
                .fillMaxWidth(WidthSizeSmall)
                .shimmer(shimmer)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
          )

          Spacer(modifier = Modifier.padding(2.dp))

          Box(
            modifier =
              Modifier.height(20.dp)
                .fillMaxWidth(WidthSizeSmall)
                .shimmer(shimmer)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
          )

          Spacer(modifier = Modifier.padding(2.dp))

          Box(
            modifier =
              Modifier.height(20.dp)
                .fillMaxWidth(WidthSizeSmall)
                .shimmer(shimmer)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
          )
        }
      }

      Spacer(modifier = Modifier.padding(8.dp))
    }
  }
}

@Composable
fun CalendarPickerCardShimmer(shimmer: Shimmer, modifier: Modifier = Modifier) {
  Card(
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = modifier.fillMaxWidth().padding(16.dp),
  ) {
    Column(modifier = Modifier.height(360.dp).fillMaxWidth().padding(16.dp)) {
      Box(
        modifier =
          Modifier.fillMaxWidth(WidthSizeSmall)
            .height(24.dp)
            .shimmer(shimmer)
            .padding(bottom = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        repeat(ItemsCount) {
          Box(
            modifier =
              Modifier.size(40.dp)
                .shimmer(shimmer)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      HorizontalDivider()

      Spacer(modifier = Modifier.height(8.dp))

      TimeSlotsBoxShimmer(shimmer = shimmer, rowsPerPage = Rows, itemsPerPage = Columns)
    }
  }
}

@Composable
fun TimeSlotsBoxShimmer(
  shimmer: Shimmer,
  rowsPerPage: Int,
  itemsPerPage: Int,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier.fillMaxWidth()) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(rowsPerPage),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(16.dp),
    ) {
      items(itemsPerPage) {
        Box(
          modifier =
            Modifier.height(40.dp)
              .fillMaxWidth(WidthSizeLarge)
              .shimmer(shimmer)
              .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
              .padding(4.dp)
        )
      }
    }
  }
}

@Preview
@Composable
private fun BookingLoadingShimmerPreview() {
  GymAppTheme {
    Surface {
      AvailabilityLoadingShimmer(
        paddingValues = PaddingValues(16.dp),
        shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View),
      )
    }
  }
}

private const val ItemsCount = 7
private const val Rows = 3
private const val Columns = 9
private const val WidthSizeSmall = 0.6f
private const val WidthSizeLarge = 0.9f
