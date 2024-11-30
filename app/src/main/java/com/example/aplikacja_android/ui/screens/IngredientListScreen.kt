package com.example.aplikacja_android.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun IngredientListScreen(navController: NavController){
    val databaseViewModel = LocalDatabaseViewModel.current
    val ingredientlist = databaseViewModel.allIgredients.observeAsState(emptyList())


    LazyColumn {
        items(ingredientlist.value){ ingredient ->
            ListItem(
                headlineContent = { Text(text = ingredient.nazwa) },
                supportingContent = { Text(text = ingredient.kategoria) },
                trailingContent = {
                    Checkbox(
                        checked = ingredient.isAvailableAtHome,
                        onCheckedChange = { isChecked ->
                            GlobalScope.launch {
                                databaseViewModel.updateIgredient(
                                    ingredient.copy(isAvailableAtHome = isChecked)
                                )
                            }
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}