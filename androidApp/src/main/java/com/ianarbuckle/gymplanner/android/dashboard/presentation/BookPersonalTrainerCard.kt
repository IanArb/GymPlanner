package com.ianarbuckle.gymplanner.android.dashboard.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ianarbuckle.gymplanner.android.R
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme

@Composable
fun BookPersonalTrainerCard(onBookPersonalTrainerClick: () -> Unit, modifier: Modifier = Modifier) {
  Column(modifier = modifier.fillMaxWidth().padding(vertical = 16.dp)) {
    Row(modifier = Modifier.fillMaxWidth()) {
      Text(
        text = "Book a personal trainer",
        style = MaterialTheme.typography.titleMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f),
      )
    }

    Spacer(modifier = Modifier.height(6.dp))

    Card(
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier.fillMaxWidth(),
    ) {
      Column {
        AsyncImage(
          modifier = Modifier.height(200.dp).fillMaxWidth(),
          model = PERSONAL_TRAINER_BACKDROP_URL,
          contentDescription = "Personal Trainer Backdrop",
          contentScale = ContentScale.Crop,
          placeholder = painterResource(id = R.drawable.ic_placeholder),
        )
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = "Need help?", fontWeight = FontWeight.Bold, fontSize = 18.sp)
          Text(text = "Book a 6-week personal training program today.")
          Spacer(modifier = Modifier.height(10.dp))
          Button(onClick = onBookPersonalTrainerClick) { Text("Book now!") }
        }
      }
    }
  }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BookPersonalTrainerCardPreview() {
  GymAppTheme {
    Surface {
      Spacer(modifier = Modifier.height(16.dp))
      BookPersonalTrainerCard(onBookPersonalTrainerClick = {})
    }
  }
}
