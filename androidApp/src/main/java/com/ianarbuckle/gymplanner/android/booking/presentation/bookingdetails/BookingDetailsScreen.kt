package com.ianarbuckle.gymplanner.android.booking.presentation.bookingdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ianarbuckle.gymplanner.android.ui.common.BottomSheet
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailsScreen(
    selectedDate: String,
    selectedTimeSlot: String,
    personalTrainerName: String,
    personalTrainerAvatarUrl: String,
    location: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    Column(modifier = modifier) {
        BottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                onDismissClick()
            },
        ) {
            BookingDetailsContent(
                selectedDate = selectedDate,
                selectedTimeSlot = selectedTimeSlot,
                personalTrainerName = personalTrainerName,
                personalTrainerAvatarUrl = personalTrainerAvatarUrl,
                location = location,
                onConfirmClick = {
                    onConfirmClick()
                },
                onCancelClick = {
                    onDismissClick()
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BookingConfirmationScreenPreview() {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    GymAppTheme {
        Column {
            BookingDetailsScreen(
                selectedDate = "2022-01-01",
                selectedTimeSlot = "10:00 - 11:00",
                personalTrainerName = "John Doe",
                personalTrainerAvatarUrl = "https://example.com/avatar.jpg",
                location = "Gym",
                onConfirmClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                },
                onDismissClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                },
                sheetState = sheetState,
            )
        }
    }
}
