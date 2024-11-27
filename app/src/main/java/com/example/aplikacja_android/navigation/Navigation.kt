package com.example.aplikacja_android.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.aplikacja_android.ui.screens.AddRecipeScreen
import com.example.aplikacja_android.ui.screens.CalendarScreen
import com.example.aplikacja_android.ui.screens.CreateShoppingListFromTemplateScreen
import com.example.aplikacja_android.ui.screens.CreateShoppingListScreen
import com.example.aplikacja_android.ui.screens.DayCalorieScreen
import com.example.aplikacja_android.ui.screens.EditRecipeScreen
import com.example.aplikacja_android.ui.screens.EditShoppingListScreen
import com.example.aplikacja_android.ui.screens.IngredientListScreen
import com.example.aplikacja_android.ui.screens.RecipeListScreen
import com.example.aplikacja_android.ui.screens.RecipeScreen
import com.example.aplikacja_android.ui.screens.RecipeWeekList
import com.example.aplikacja_android.ui.screens.ShoppingListScreen
import com.example.aplikacja_android.ui.screens.TipScreen
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
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
        composable(Screens.ShoppingListScreen.route){
            ShoppingListScreen(navController  = navcontroller)
        }
        composable(Screens.IngredientList.route){
            IngredientListScreen(navController = navcontroller)
        }
        composable(Screens.CreateShoppingListScreen.route){
            CreateShoppingListScreen(navController = navcontroller)
        }
        composable(
            route=Screens.EditShoppingListScreen.route,
            arguments = listOf(navArgument("listID") { type = NavType.IntType })
        ){backStackEntry ->
            val databaseViewModel = LocalDatabaseViewModel.current
            val shoppingListId = backStackEntry.arguments?.getInt("listID")
            val shoppingList = databaseViewModel.selectedList.value

            if (shoppingList != null && shoppingList.id == shoppingListId) {
                EditShoppingListScreen(navController = navcontroller, shoppingList = shoppingList)
            }
        }
        composable(
            route = Screens.CreateShoppingListFromTemplateScreen.route,
            arguments = listOf(navArgument("listID") { type = NavType.IntType })
        ){backStackEntry ->
            val databaseViewModel = LocalDatabaseViewModel.current
            val shoppingListId = backStackEntry.arguments?.getInt("listID")
            val shoppingList = databaseViewModel.selectedList.value
            if (shoppingList != null && shoppingList.id == shoppingListId) {
                CreateShoppingListFromTemplateScreen(navController = navcontroller, shoppingList = shoppingList)
            }
        }
        composable(Screens.TipScreen.route){
            TipScreen(navController = navcontroller)
        }
        composable(
            route = Screens.DayCalorieScreen.route,
            arguments = listOf(
                navArgument("day") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType },
                navArgument("year") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val day = backStackEntry.arguments?.getInt("day") ?: 1
            val month = backStackEntry.arguments?.getInt("month") ?: 1
            val year = backStackEntry.arguments?.getInt("year") ?: 2024

            val localDate = LocalDate.of(year, month, day)

            // Pass the nutrients to the DayCalorieScreen Composable
            DayCalorieScreen(navController = navcontroller,localDate = localDate)
        }
    }
}