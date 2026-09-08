package com.ianarbuckle.gymplanner.web.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translate by
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1200f,
            animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 1400, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "shimmer_translate",
        )
    return Brush.linearGradient(
        colors = listOf(Color(0xFFE6E6E6), Color(0xFFF5F5F5), Color(0xFFE6E6E6)),
        start = Offset(x = translate - 600f, y = 0f),
        end = Offset(x = translate, y = 0f),
    )
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    brush: Brush,
) {
    Box(modifier = modifier.clip(shape).background(brush))
}
