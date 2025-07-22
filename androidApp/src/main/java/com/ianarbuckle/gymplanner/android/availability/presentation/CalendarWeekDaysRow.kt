package com.ianarbuckle.gymplanner.android.availability.presentation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider.daysOfWeek
import com.ianarbuckle.gymplanner.android.utils.convertDate
import com.ianarbuckle.gymplanner.android.utils.isCurrentDay

@Composable
fun CalendarWeekDaysRow(
    pagerState: PagerState,
    daysOfWeek: List<String>,
    selectedDate: String,
    onSelectedDateChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        HorizontalPager(state = pagerState) { page ->
            val startIndex = page * PageSize
            val endIndex = (startIndex + PageSize).coerceAtMost(daysOfWeek.size)
            val daysSubset = daysOfWeek.subList(startIndex, endIndex)

            LazyVerticalGrid(
                columns = GridCells.Fixed(PageSize),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.testTag(CalendarGridTestTag).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(daysSubset) { index, day ->
                    val isCurrentDay = day.isCurrentDay() && selectedDate.isEmpty()

                    Box(
                        modifier =
                            Modifier.clickable { onSelectedDateChange(day) }
                                .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        val textColor =
                            when {
                                isCurrentDay -> MaterialTheme.colorScheme.primary
                                selectedDate == day -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        val primary = MaterialTheme.colorScheme.primary

                        val formatDay = convertDate(day)

                        Text(
                            text = formatDay,
                            textAlign = TextAlign.Center,
                            color = textColor,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier =
                                Modifier.drawBehind {
                                    // Draw a line underneath the text
                                    val lineThickness = 2.dp.toPx() // Thickness of the underline
                                    val yOffset = size.height // Line position just below the text
                                    if (isCurrentDay || selectedDate == day) {
                                        drawLine(
                                            color = primary,
                                            start = Offset(0f, yOffset),
                                            end = Offset(size.width, yOffset),
                                            strokeWidth = lineThickness,
                                        )
                                    }
                                },
                        )
                    }
                }
            }
        }
    }
}

const val CalendarGridTestTag = "CalendarGridTestTag"

@Preview(showBackground = true, name = "Light mode")
@Preview(showBackground = true, name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CalendarWeekDaysRowPreview() {
    GymAppTheme {
        val pagerState = rememberPagerState {
            PageSize // Calculate the number of pages needed
        }
        Surface {
            CalendarWeekDaysRow(
                pagerState = pagerState,
                daysOfWeek = daysOfWeek,
                selectedDate = "2024-12-12",
                onSelectedDateChange = {},
            )
        }
    }
}
