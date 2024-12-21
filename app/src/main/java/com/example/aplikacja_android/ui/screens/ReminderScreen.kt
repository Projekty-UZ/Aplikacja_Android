package com.example.aplikacja_android.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.aplikacja_android.database.models.Reminder
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val databaseViewModel = LocalDatabaseViewModel.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dayOfWeek by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var repeat by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create Reminder") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default
            )

            OutlinedTextField(
                value = dayOfWeek,
                onValueChange = { dayOfWeek = it },
                label = { Text("Day of Week (e.g., Monday)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default
            )

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time (e.g., 08:30)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )


            Button (
                onClick = {
                    if (title.isBlank() || description.isBlank() || dayOfWeek.isBlank() || time.isBlank()) {

                        return@Button
                    }

                    // Insert reminder into database
                    scope.launch {
                        val reminder = Reminder(
                            title = title,
                            description = description,
                            dayOfWeek = dayOfWeek,
                            time = time
                        )
                        val newReminder = databaseViewModel.createReminder(reminder)

                        databaseViewModel.scheduleReminder(context = navController.context, reminder = newReminder ?: return@launch)
                        navController.popBackStack()
                    }
                }
            ) {
                Text("Create Reminder")
            }
        }
    }
}