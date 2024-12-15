package com.ianarbuckle.gymplanner.android.booking.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider.daysOfWeek
import com.ianarbuckle.gymplanner.android.utils.currentMonth
import java.util.Calendar

@Composable
fun CalendarHeader(
    daysOfWeek: List<String>,
    pagerState: PagerState,
    selectedDate: String,
    modifier: Modifier = Modifier,
    onSelectedDateChange: (String) -> Unit,
) {
    Text(
        text = "Available Time",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val calendarMonth = Calendar.getInstance().currentMonth()
        Text(
            text = calendarMonth,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.weight(1f)
        )
    }

    CalendarWeekDaysRow(
        pagerState = pagerState,
        daysOfWeek = daysOfWeek,
        selectedDate = selectedDate,
        modifier = modifier,
        onSelectedDateChange = onSelectedDateChange,
    )

    Spacer(modifier = Modifier.padding(2.dp))

    HorizontalDivider()
}

@Preview(showBackground = true, name = "Light mode")
@Preview(showBackground = true, name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CalendarHeaderPreview() {
    GymAppTheme {
        val pagerState = rememberPagerState {
            5 // Calculate the number of pages needed
        }
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            CalendarHeader(
                daysOfWeek = daysOfWeek,
                pagerState = pagerState,
                selectedDate = "2024-12-12",
                onSelectedDateChange = { }
            )
        }
    }
}