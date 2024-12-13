package com.ianarbuckle.gymplanner.android.booking.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun BookingLoadingShimmer(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.padding(paddingValues)
    ) {
        PersonalTrainerCard(
            personalTrainerLabel = "",
            imageUrl = "",
            name = "",
            qualifications = emptyList(),
            isAvailable = false,
            modifier = Modifier.shimmer()
        )
    }
}

@Preview
@Composable
fun BookingLoadingShimmerPreview() {
    GymAppTheme {
        Scaffold {
            BookingLoadingShimmer(
                paddingValues = it
            )
        }
    }

}