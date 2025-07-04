package com.ianarbuckle.gymplanner.android.availability.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import kotlinx.coroutines.launch

@Composable
fun NextPrevIndicators(pagerState: PagerState, modifier: Modifier = Modifier) {
  val scope = rememberCoroutineScope()
  Row(modifier = modifier) {
    IconButton(
      onClick = {
        if (pagerState.currentPage > 0) {
          scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
        }
      }
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "Previous",
        modifier = Modifier.size(18.dp),
        tint = MaterialTheme.colorScheme.onSurface,
      )
    }

    IconButton(
      onClick = {
        if (pagerState.currentPage < pagerState.pageCount - 1) {
          scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
        }
      }
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = "Next",
        modifier = Modifier.size(18.dp),
        tint = MaterialTheme.colorScheme.onSurface,
      )
    }
  }
}

@Preview(showBackground = true, name = "Light mode")
@Preview(showBackground = true, name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NextPreviousIndicatorsPreview() {
  val pagerState = rememberPagerState { PageSize }

  GymAppTheme { Surface { NextPrevIndicators(pagerState = pagerState) } }
}

const val PageSize = 5
