package com.ianarbuckle.gymplanner.android.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

@Composable
fun Modifier.shimmerEffect(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(AnimationDuration, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "shimmerProgress",
        )

    return this.then(ShimmerElement(progress))
}

private data class ShimmerElement(
    private val progress: Float,
    private val inboundColor: Long = InboundColor,
    private val outboundColor: Long = OutboundColor,
) : ModifierNodeElement<ShimmerNode>() {
    override fun create() =
        ShimmerNode(progress = progress, inboundColor = inboundColor, outboundColor = outboundColor)

    override fun update(node: ShimmerNode) {
        node.progress = progress
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "shimmerEffect"
    }
}

private class ShimmerNode(var progress: Float, val inboundColor: Long, val outboundColor: Long) :
    DrawModifierNode, Modifier.Node() {

    override fun ContentDrawScope.draw() {
        val width = size.width
        val height = size.height
        val startOffsetX = ShimmerStartOffset * width + (ShimmerEndOffset * width * progress)

        val brush =
            Brush.linearGradient(
                colors = listOf(Color(inboundColor), Color(outboundColor), Color(inboundColor)),
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + width, height),
            )

        drawRect(brush = brush)
        drawContent()
    }
}

const val InboundColor = 0xFFB8B5B5
const val OutboundColor = 0xFF8F8B8B

const val AnimationDuration = 1000

const val ShimmerStartOffset = -2f
const val ShimmerEndOffset = 4f
