package com.example.aplikacja_android.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

@SuppressLint("NewApi")
@Composable
fun CalendarScreen() {
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }
    var currentMonth by remember { mutableStateOf(LocalDate.now().monthValue) }

    val daysOfMonth = getDaysInMonth(currentYear, currentMonth)
    val monthName = getMonthName(currentYear, currentMonth)

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                // Navigate to the previous month
                if (currentMonth == 1) {
                    currentMonth = 12
                    currentYear -= 1
                } else {
                    currentMonth -= 1
                }
            }) {
                Text("Previous")
            }

            // Display current month and year
            Text(
                text = "$monthName $currentYear",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Button(onClick = {
                // Navigate to the next month
                if (currentMonth == 12) {
                    currentMonth = 1
                    currentYear += 1
                } else {
                    currentMonth += 1
                }
            }) {
                Text("Next")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        // Display day names
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { dayName ->
                Text(
                    text = dayName,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display the days in a grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(daysOfMonth.size) { index ->
                val day = daysOfMonth[index]
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp).clickable {  },
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = day?.toString() ?: "",
                        style = TextStyle(color = Color.White)
                    )
                }
            }
        }
    }
}

@SuppressLint("NewApi")
fun getDaysInMonth(year: Int, month: Int): List<Int?> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Adjust to make Sunday=0

    // Create a list to hold days, with leading nulls for the offset
    val days = MutableList<Int?>(startDayOfWeek) { null }
    days.addAll((1..daysInMonth).toList())

    return days
}

@SuppressLint("NewApi")
fun getMonthName(year: Int, month: Int): String {
    return YearMonth.of(year, month).month.name.lowercase().replaceFirstChar { it.uppercase() }
}