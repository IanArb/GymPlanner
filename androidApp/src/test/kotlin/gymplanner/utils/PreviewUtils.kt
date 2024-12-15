package gymplanner.utils

import androidx.compose.runtime.Composable
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun ScreenTestPreview(
    isDarkTheme: Boolean = false,
    slot: @Composable () -> Unit = {}
) {
    GymAppTheme(
        darkTheme = isDarkTheme
    ) {
        slot()
    }
}