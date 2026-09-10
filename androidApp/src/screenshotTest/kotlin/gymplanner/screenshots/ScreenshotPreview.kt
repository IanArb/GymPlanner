package gymplanner.screenshots

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

/**
 * Renders every screenshot in light mode, dark mode and at a 1.5x font scale.
 *
 * The names are kept free of spaces because they end up in the reference image file names under
 * `src/screenshotTestDebug/reference/`.
 */
@Preview(name = "light", showBackground = true)
@Preview(name = "dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "largeFont", showBackground = true, fontScale = 1.5f)
annotation class ScreenshotPreviews

/**
 * Common wrapper for every screenshot test preview.
 *
 * Dynamic colour is disabled so the reference images always render the app's own palette, which
 * keeps them reproducible across host machines and API levels.
 */
@Composable
fun ScreenshotPreview(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    GymAppTheme(dynamicColor = false) { Surface(modifier = modifier) { content() } }
}
