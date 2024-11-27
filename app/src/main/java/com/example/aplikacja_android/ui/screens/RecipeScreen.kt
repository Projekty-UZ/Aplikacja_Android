package com.example.aplikacja_android.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikacja_android.R
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.navigation.Screens
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel

@Composable
fun RecipeScreen(recipe: Recipe,navController: NavController){

    val scrollState = rememberScrollState()
    val databaseViewModel = LocalDatabaseViewModel.current
    val recipeIngredients = databaseViewModel.getIngredientsOfRecipe(recipe.id).observeAsState(emptyList())
    val nutrientValues = databaseViewModel.calculateRecipeNutrients(recipe.id).observeAsState(emptyMap())



    Column (
        modifier = Modifier.
        fillMaxWidth().
        verticalScroll(scrollState).
        padding(16.dp,30.dp,16.dp,16.dp)
    ){
        Text(text=recipe.nazwa)
        Text(text=recipe.instrukcja)
        Text(text = recipe.rodzaj)

        recipeIngredients.value.forEach{ ingredient ->
            Text(text= ingredient.ingredient.nazwa+","+ingredient.ilosc+","+ingredient.unit.jednostka)
        }

        nutrientValues.value.let { nutrients ->
            Text(text = "Total Nutritional Values:")
            Text(text = "Kalorie: ${"%.2f".format(nutrients["kalorie"] ?: 0.0)} kcal")
            Text(text = "Białko: ${"%.2f".format(nutrients["bialko"] ?: 0.0)} g")
            Text(text = "Tłuszcz: ${"%.2f".format(nutrients["tluszcz"] ?: 0.0)} g")
            Text(text = "Węglowodany: ${"%.2f".format(nutrients["weglowodany"] ?: 0.0)} g")
        }

    }
    FloatingActionButton(
        modifier = Modifier.padding(300.dp,650.dp,0.dp,0.dp),
        onClick = {
            navController.navigate(Screens.EditRecipeScreen.createRoute(recipe.id))
                  },
    ) {
        Text(text = "Edit Recipe")
    }
}