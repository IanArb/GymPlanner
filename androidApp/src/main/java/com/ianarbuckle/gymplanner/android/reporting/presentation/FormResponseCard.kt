package com.ianarbuckle.gymplanner.android.reporting.presentation

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport

@Composable
fun FormResponseCard(
    faultReport: FaultReport,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Your report",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
            )

            Spacer(modifier = Modifier.padding(12.dp))

            Text(
                text = "Machine number",
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.padding(2.dp))

            Spacer(modifier = Modifier.padding(2.dp))

            Text(
                text = faultReport.machineNumber.toString(),
                fontWeight = FontWeight.Normal,
            )

            Spacer(modifier = Modifier.padding(6.dp))

            Text(
                text = "Description",
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.padding(2.dp))

            Text(
                text = faultReport.description,
                fontWeight = FontWeight.Normal,
            )

            Spacer(modifier = Modifier.padding(12.dp))

            ImageFromUri(
                uri = Uri.parse(faultReport.photoUri),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            )

            Spacer(modifier = Modifier.padding(6.dp))

            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Report again",
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FormResponseCardPreview() {
    GymAppTheme {
        Surface {
            FormResponseCard(
                faultReport = FaultReport(
                    machineNumber = 1,
                    description = "description",
                    photoUri = "https://www.example.com/image.jpg",
                    date = "2022-01-01",
                ),
                onClick = {
                    // Handle click
                }
            )
        }
    }
}
