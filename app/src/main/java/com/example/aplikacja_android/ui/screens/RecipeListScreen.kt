package com.example.aplikacja_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikacja_android.R
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.navigation.Screens
import com.example.aplikacja_android.ui.viewModels.DatabaseViewModel
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel

@Composable
fun RecipeListScreen(navController: NavController){
    val databaseViewModel = LocalDatabaseViewModel.current
    val allRecipes = databaseViewModel.allRecipes.observeAsState(emptyList())

    var isSortedAlphabetically by remember { mutableStateOf(false) }

    val displayedRecipes = if (isSortedAlphabetically) {
        allRecipes.value.sortedBy { it.nazwa }
    } else {
        allRecipes.value
    }

    LazyColumn(
        modifier = Modifier.
        fillMaxSize().
        padding(0.dp,20.dp,0.dp,0.dp),
    ) {
        items(displayedRecipes) { recipe ->
            ListedRecipe(recipe,databaseViewModel, navController)
        }
    }
    FloatingActionButton(
        modifier = Modifier.padding(300.dp,650.dp,0.dp,0.dp),
        onClick = { navController.navigate(Screens.AddRecipeScreen.route) },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.add),
            contentDescription = "AddButton",
            modifier = Modifier.size(50.dp)
        )
    }
    FloatingActionButton(
        modifier = Modifier.padding(50.dp,650.dp,0.dp,0.dp),
        onClick = { isSortedAlphabetically = !isSortedAlphabetically },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.sort),
            contentDescription = "SortButton",
            modifier = Modifier.size(50.dp)
        )
    }
}

@Composable
fun ListedRecipe(recipe: Recipe,databaseViewModel: DatabaseViewModel,navController: NavController){
    ListItem(
        modifier = Modifier.clickable {
            databaseViewModel.selectRecipe(recipe)
            navController.navigate(Screens.RecipeScreen.createRoute(recipe.id))
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.recipes),
                contentDescription = "Icon",
                modifier = Modifier.size(50.dp)
            )
        },
        headlineContent = {
            Text(
                text = recipe.nazwa
            )
        },
        supportingContent = {
            Text(
                text = recipe.rodzaj,
            )
        },

    )
    Divider()
}