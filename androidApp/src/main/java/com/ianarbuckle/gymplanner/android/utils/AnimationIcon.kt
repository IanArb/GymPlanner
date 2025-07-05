package com.ianarbuckle.gymplanner.android.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedIcon(
  state: AnimatedIconState,
  modifier: Modifier = Modifier,
  tintColor: Color = MaterialTheme.colorScheme.primary,
) {
  // Trigger animation when composable enters composition
  val scale = remember { Animatable(state.initialScale) }

  LaunchedEffect(Unit) {
    scale.animateTo(
      targetValue = state.targetScale,
      animationSpec = spring(dampingRatio = state.dampingRatio, stiffness = state.stiffness),
    )
  }

  Icon(
    imageVector = state.vectorIcon,
    contentDescription = state.contentDescription,
    tint = tintColor,
    modifier =
      modifier.size(state.iconSize).graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
      },
  )
}

@Preview
@Composable
private fun AnimatedIconPreview() {
  AnimatedIcon(state = AnimatedIconState())
}

data class AnimatedIconState(
  val vectorIcon: ImageVector = Icons.Default.CheckCircle,
  val initialScale: Float = 0f,
  val targetScale: Float = 1f,
  val contentDescription: String = "Success",
  val iconSize: Dp = 64.dp,
  val dampingRatio: Float = Spring.DampingRatioMediumBouncy,
  val stiffness: Float = Spring.StiffnessLow,
)
