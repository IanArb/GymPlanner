package com.ianarbuckle.gymplanner.android.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource

/**
 * A sealed interface to represent an icon source, which can be either a drawable resource or an
 * [ImageVector]. This allows for a flexible and stable representation of icons in data classes.
 */
sealed interface IconSource {
    data class FromVector(val imageVector: ImageVector) : IconSource

    data class FromResource(@DrawableRes val id: Int) : IconSource

    @Composable
    fun asPainter(): Painter {
        return when (this) {
            is FromVector -> rememberVectorPainter(imageVector)
            is FromResource -> painterResource(id)
        }
    }
}
