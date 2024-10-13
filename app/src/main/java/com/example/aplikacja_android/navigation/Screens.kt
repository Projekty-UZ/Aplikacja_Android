package com.example.aplikacja_android.navigation

import com.example.aplikacja_android.database.models.Recipe

//przypisanie kazdego scrrenu aplikacji do obiektu
sealed class Screens (val route:String){
    object AddRecipeScreen: Screens("addrecipe");
    object RecipeScreen: Screens("recipe/{recipeId}"){
        fun createRoute(recipeId: Int) = "recipe/${recipeId}"
    }
    object EditRecipeScreen: Screens("editrecipe/{recipeId}"){
        fun createRoute(recipeId: Int) = "editrecipe/${recipeId}"
    }
    object RecipeListScreen: Screens("recipelist");
    object CalendarScreen: Screens("calendar");

}