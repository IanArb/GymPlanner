package com.ianarbuckle.gymplanner.android.ui.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
        modifier = modifier,
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewsCombined
@Composable
private fun BookingBottomSheetPreview() {
    GymAppTheme {
        BottomSheet(sheetState = rememberModalBottomSheetState(), onDismissRequest = {}) {}
    }
}
