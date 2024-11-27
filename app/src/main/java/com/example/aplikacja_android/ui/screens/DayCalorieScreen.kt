package com.example.aplikacja_android.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import java.time.LocalDate

@Composable
fun DayCalorieScreen(navController: NavController, localDate: LocalDate) {
    val databaseViewModel = LocalDatabaseViewModel.current
    val nutrientValues = databaseViewModel.calculateDayNutrients(localDate).observeAsState()


    val recommendedDailyIntake = mapOf(
        "kalorie" to 2000.0,    // kcal
        "bialko" to 50.0,       // g
        "tluszcz" to 70.0,      // g
        "weglowodany" to 300.0  // g
    )

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("$localDate nutrients consumption", fontSize = 30.sp)
        // Display the nutrients
        nutrientValues.value?.let { nutrients ->
            recommendedDailyIntake.forEach { (nutrient, rdi) ->
                val consumed = nutrients[nutrient] ?: 0.0
                val percentage = (consumed / rdi * 100) // No cap for percentage here

                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "$nutrient: ${"%.2f".format(consumed)} / ${"%.2f".format(rdi)}",
                        fontSize = 20.sp
                    )
                    // Progress Bar shows max of 1.5 times RDI for visualization clarity
                    LinearProgressIndicator(
                        progress = { (consumed / rdi).toFloat().coerceIn(0f, 1.5f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        color = when {
                            percentage > 120.0 -> Color.Red
                            percentage >= 105.0 -> Color.Yellow
                            else -> Color.Green
                        },
                    )
                    Text(
                        text = "${"%.2f".format(percentage)}% of recommended intake",
                        fontSize = 14.sp,
                        color = when {
                            percentage > 120.0 -> Color.Red
                            percentage >= 105.0 -> Color.Yellow
                            percentage >= 90.0 -> Color.Green
                            else -> Color.Gray
                        }
                    )
                }
            }
        }

        // Add any other UI components or actions here
    }
}