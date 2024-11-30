package com.example.aplikacja_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.aplikacja_android.R
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.navigation.Screens
import com.example.aplikacja_android.ui.viewModels.DatabaseViewModel
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(navController: NavController){
    val databaseViewModel = LocalDatabaseViewModel.current
    val allRecipes = databaseViewModel.allRecipes.observeAsState(emptyList())
    val allIngredients = databaseViewModel.allIgredients.observeAsState(emptyList())
    val lifecycleOwner = LocalLifecycleOwner.current

    var recipesWithChosenIngredient by remember { mutableStateOf(mutableListOf<Recipe>(Recipe(0,"","",""))) }
    var isSortedAlphabetically by remember { mutableStateOf(false) }
    var isFilteredByIngredient by remember { mutableStateOf(false) }


    var chosenIngredient by remember { mutableStateOf<Igredient?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val displayedRecipes = if (isSortedAlphabetically) {
        if(chosenIngredient !== null){
            recipesWithChosenIngredient.sortedBy { it.nazwa }
        }else {
            allRecipes.value.sortedBy { it.nazwa }
        }
    } else {
        if(chosenIngredient !== null){
            recipesWithChosenIngredient
        }
        else {
            allRecipes.value
        }
    }

    LazyColumn(
        modifier = Modifier.
        fillMaxSize().
        padding(0.dp,20.dp,0.dp,0.dp),
    ) {
        item{
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.padding(20.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    value = chosenIngredient?.nazwa ?: "Filter by Ingredient",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {

                    DropdownMenuItem(
                        text = { Text(text="All Recipes")},
                        onClick = {
                            chosenIngredient = null
                            expanded = false
                        }
                    )
                    allIngredients.value.forEach { ingredientOption ->
                        DropdownMenuItem(
                            text = { Text(text = ingredientOption.nazwa) },
                            onClick = {
                                chosenIngredient = ingredientOption
                                expanded = false

                                databaseViewModel.getRecipesWithIngredient(ingredientOption.id).observe(lifecycleOwner) { recipes ->
                                    // Check if recipes is not null and update the list
                                    recipes?.let {
                                        recipesWithChosenIngredient = it.toMutableList()
                                    } ?: run {
                                        // Handle null case, maybe set an empty list
                                        recipesWithChosenIngredient = mutableListOf()
                                    }
                                }
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }
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
        trailingContent = {
            Button(
                onClick = {
                    GlobalScope.launch {
                        databaseViewModel.deleteRecipesWithCrossRef(recipe)
                    }
                }
            ) {
                Text(text="Delete")
            }
        }
    )
    Divider()
}

