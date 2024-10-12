package com.example.aplikacja_android.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel

@Composable
fun RecipeListScreen(navController: NavController){
    val databaseViewModel = LocalDatabaseViewModel.current
    val allRecipes = databaseViewModel.allIgredients.observeAsState(emptyList())
    fun addRecipe(){
        databaseViewModel.addRecipe()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { addRecipe() }) {
            Text(text = "Add New Recipe")
        }
        LazyColumn {
            items(allRecipes.value){recipe->
                Text(
                    text = recipe.nazwa
                )

            }
        }
    }
}