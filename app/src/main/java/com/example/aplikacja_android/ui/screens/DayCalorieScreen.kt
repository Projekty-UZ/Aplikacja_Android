package com.example.aplikacja_android.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import java.time.LocalDate

@Composable
fun DayCalorieScreen(navController: NavController, localDate: LocalDate) {
    val databaseViewModel = LocalDatabaseViewModel.current
    val nutrientValues = databaseViewModel.calculateDayNutrients(localDate).observeAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("$localDate nutrients consumption", fontSize = 30.sp)
        // Display the nutrients
        Text("Calories: ${"%.2f".format(nutrientValues.value?.get("kalorie") ?: 0.0)} kcal")
        Text("Protein: ${"%.2f".format(nutrientValues.value?.get("bialko") ?: 0.0)} g")
        Text("Fat: ${"%.2f".format(nutrientValues.value?.get("tluszcz") ?: 0.0)} g")
        Text("Carbs: ${"%.2f".format(nutrientValues.value?.get("weglowodany") ?: 0.0)} g")

        // Add any other UI components or actions here
    }
}