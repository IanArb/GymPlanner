package com.ianarbuckle.gymplanner.android.dashboard.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.displayTime
import com.ianarbuckle.gymplanner.android.utils.toLocalTime
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymClassesCarousel(
  classesCarouselItems: ImmutableList<FitnessClass>,
  modifier: Modifier = Modifier,
) {
  if (classesCarouselItems.isEmpty()) {
    Text(
      text = "No classes available at the moment.",
      modifier = Modifier.padding(16.dp),
      style = MaterialTheme.typography.bodyLarge,
    )
  } else {
    HorizontalUncontainedCarousel(
      state = rememberCarouselState { classesCarouselItems.count() },
      itemSpacing = 8.dp,
      itemWidth = 320.dp,
      modifier = modifier,
    ) { index ->
      val item = classesCarouselItems[index]

      Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxHeight(),
      ) {
        AsyncImage(
          modifier = Modifier.height(200.dp).fillMaxWidth(),
          model = item.imageUrl,
          contentDescription = null,
          contentScale = ContentScale.Crop,
          placeholder = painterResource(id = R.drawable.ic_placeholder),
        )
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
          Text(text = item.description)

          Spacer(modifier = Modifier.padding(10.dp))

          val startDisplayTime = item.startTime.toLocalTime().displayTime()
          val endDisplayTime = item.endTime.toLocalTime().displayTime()

          val classTime = "$startDisplayTime - $endDisplayTime"

          Row {
            Icon(painterResource(id = R.drawable.ic_clock), contentDescription = null)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = classTime)
          }

          Spacer(modifier = Modifier.padding(4.dp))
        }
      }
    }
  }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GymClassesCarouselPreview() {
  GymAppTheme {
    Surface(modifier = Modifier.height(350.dp).fillMaxWidth()) {
      GymClassesCarousel(classesCarouselItems = DataProvider.fitnessClasses())
    }
  }
}
