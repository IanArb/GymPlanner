package com.ianarbuckle.gymplanner.android.chat.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun ConnectionBanner(
  visible: Boolean,
  connectionText: String,
  backgroundColor: Color,
  textColor: Color,
  iconColor: Color,
  modifier: Modifier = Modifier,
) {
  AnimatedVisibility(visible = visible, exit = fadeOut(), modifier = modifier) {
    Box(
      modifier =
        Modifier.fillMaxWidth().drawBehind {
          drawRoundRect(
            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
            color = backgroundColor,
          )
        }
    ) {
      Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Absolute.Center,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(text = connectionText, color = textColor)
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
          imageVector = Icons.Filled.Refresh,
          contentDescription = "Connection status icon",
          tint = iconColor,
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ConnectionBannerPreview() {
  val isDark = isSystemInDarkTheme()
  val backgroundColor =
    if (isDark) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
  val textColor =
    if (isDark) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface
  val iconColor = textColor
  var showBanner by remember { mutableStateOf(true) }

  GymAppTheme {
    Scaffold(topBar = { TopAppBar(title = { Text("Connection Banner Preview") }) }) { padding ->
      Surface(modifier = Modifier.padding(padding).fillMaxWidth().padding(16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          ConnectionBanner(
            visible = showBanner,
            connectionText = "Connection Lost",
            backgroundColor = backgroundColor,
            textColor = textColor,
            iconColor = iconColor,
          )
        }
      }
    }
  }
}
