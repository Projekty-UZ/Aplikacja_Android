package com.example.aplikacja_android.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.ui.screens.AddRecipeScreen
import com.example.aplikacja_android.ui.screens.CalendarScreen
import com.example.aplikacja_android.ui.screens.EditRecipeScreen
import com.example.aplikacja_android.ui.screens.RecipeListScreen
import com.example.aplikacja_android.ui.screens.RecipeScreen
import com.example.aplikacja_android.ui.screens.RecipeWeekList
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel

@Composable
fun Navigation(navcontroller:NavHostController){
    NavHost(
        navController = navcontroller,
        startDestination = Screens.RecipeListScreen.route,
        enterTransition = { EnterTransition.None},
        exitTransition = { ExitTransition.None}
    ){
        composable(Screens.RecipeListScreen.route){
            RecipeListScreen(navcontroller)
        }
        composable(Screens.CalendarScreen.route) {
            CalendarScreen(navcontroller)
        }
        composable(Screens.AddRecipeScreen.route) {
            AddRecipeScreen(navcontroller)
        }
        composable(
            route=Screens.EditRecipeScreen.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ){backStackEntry ->
            val databaseViewModel = LocalDatabaseViewModel.current
            val recipeId = backStackEntry.arguments?.getInt("recipeId")
            val recipe = databaseViewModel.selectedRecipe.value

            if (recipe != null && recipe.id == recipeId) {
                EditRecipeScreen(navController = navcontroller, recipe = recipe)
            }
        }
        composable(
            route=Screens.RecipeScreen.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ){backStackEntry ->
            val databaseViewModel = LocalDatabaseViewModel.current
            val recipeId = backStackEntry.arguments?.getInt("recipeId")
            val recipe = databaseViewModel.selectedRecipe.value

            if (recipe != null && recipe.id == recipeId) {
                RecipeScreen(navController = navcontroller, recipe = recipe)
            }
        }
        composable(
            route = Screens.RecipeWeekList.route,
            arguments = listOf(
                navArgument("startweek"){type = NavType.LongType},
                navArgument("endweek"){type = NavType.LongType})
        ){backStackEntry ->
            val startWeek = backStackEntry.arguments?.getLong("startweek")
            val endWeek = backStackEntry.arguments?.getLong("endweek")
            if(startWeek != null && endWeek != null) {
                RecipeWeekList(startWeek = startWeek, endWeek = endWeek)
            }
        }
    }
}