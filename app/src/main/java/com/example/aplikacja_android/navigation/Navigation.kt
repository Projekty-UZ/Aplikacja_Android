package com.example.aplikacja_android.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.aplikacja_android.ui.screens.CalendarScreen
import com.example.aplikacja_android.ui.screens.RecipeListScreen
import com.example.aplikacja_android.ui.screens.RecipeScreen

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
            CalendarScreen()
        }
        composable(Screens.RecipeScreen.route) {
            RecipeScreen()
        }
    }
}