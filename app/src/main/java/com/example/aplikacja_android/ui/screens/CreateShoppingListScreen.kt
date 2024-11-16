package com.example.aplikacja_android.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikacja_android.database.models.CalendarMeal
import com.example.aplikacja_android.navigation.Screens
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateShoppingListScreen(navController: NavController){
    val databaseViewModel = LocalDatabaseViewModel.current

    var currentYear by remember { mutableStateOf(LocalDate.now().year) }
    var currentMonth by remember { mutableStateOf(LocalDate.now().monthValue) }

    var startDate: LocalDate? by remember { mutableStateOf(null) }
    var endDate: LocalDate? by remember { mutableStateOf(null) }

    val daysOfMonth = getDaysInMonth(currentYear, currentMonth)
    val monthName = getMonthName(currentYear, currentMonth)
    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {

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
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(daysOfMonth.size) { index ->
                val day = daysOfMonth[index]
                val date = day?.let { LocalDate.of(currentYear, currentMonth, it) }
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .background(
                            color = when (date) {
                                startDate -> Color.Green
                                endDate -> Color.Red
                                else -> Color.Transparent
                            },
                            shape = CircleShape
                        )
                        .clickable {
                            if (date != null) {
                                when {
                                    startDate == null -> startDate = date
                                    endDate == null -> {
                                        endDate = date
                                        // Ensure startDate is earlier than endDate
                                        if (startDate != null && endDate != null && endDate!! < startDate!!) {
                                            val temp = startDate
                                            startDate = endDate
                                            endDate = temp
                                        }
                                    }

                                    else -> {
                                        // Reset if both are already selected
                                        startDate = date
                                        endDate = null
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = day?.toString() ?: "",
                        style = TextStyle(
                            color = when (date) {
                                startDate, endDate -> Color.Black
                                else -> Color.White
                            }
                        )
                    )
                }

            }
        }
        Button(
            onClick = {
                if (startDate != null && endDate != null) {
                    GlobalScope.launch{
                        databaseViewModel.generateShoppingListForDateRange(startDate!!,endDate!!)
                    }
                    navController.navigate(Screens.ShoppingListScreen.route)
                }
            },
            enabled = startDate != null && endDate != null
        ) {
            Text("Create Shopping List From Selected Range")
        }
    }
}