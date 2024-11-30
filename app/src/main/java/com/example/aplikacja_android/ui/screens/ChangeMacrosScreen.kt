package com.example.aplikacja_android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplikacja_android.database.models.Macros
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun ChangeMacrosScreen(navController: NavController) {
    val databaseViewModel = LocalDatabaseViewModel.current
    val macros = databaseViewModel.getMacros().value

    var calories by remember { mutableStateOf(macros?.calories.toString()) }
    var protein by remember { mutableStateOf(macros?.protein.toString()) }
    var fat by remember { mutableStateOf(macros?.fat.toString()) }
    var carbohydrates by remember { mutableStateOf(macros?.carbohydrates.toString()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Change Macros", fontSize = 30.sp)

        // Input fields for macros
        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = protein,
            onValueChange = { protein = it },
            label = { Text("Protein (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = fat,
            onValueChange = { fat = it },
            label = { Text("Fat (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = carbohydrates,
            onValueChange = { carbohydrates = it },
            label = { Text("Carbohydrates (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Save button
        Button(
            onClick = {
                val updatedMacros = Macros(
                    0,
                    calories = calories.toDoubleOrNull() ?: macros?.calories ?: 0.0,
                    protein = protein.toDoubleOrNull() ?: macros?.protein ?: 0.0,
                    fat = fat.toDoubleOrNull() ?: macros?.fat ?: 0.0,
                    carbohydrates = carbohydrates.toDoubleOrNull() ?: macros?.carbohydrates ?: 0.0
                )
                GlobalScope.launch {
                    databaseViewModel.updateMacros(updatedMacros)
                }
                navController.popBackStack() // Navigate back after saving
            }
        ) {
            Text("Save Changes")
        }
    }
}