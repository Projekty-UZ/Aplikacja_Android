package com.example.aplikacja_android.navigation

//przypisanie kazdego scrrenu aplikacji do obiektu
sealed class Screens (val route:String){
    object AddRecipeScreen: Screens("addrecipe");
    object RecipeScreen: Screens("recipe");
    //object EditRecipeScreen: Screens("editrecipe");
    object RecipeListScreen: Screens("recipelist");
    object CalendarScreen: Screens("calendar");

}