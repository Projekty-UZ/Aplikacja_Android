package com.example.aplikacja_android.navigation

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
    object RecipeWeekList: Screens("recipeweeklist/{startweek}/{endweek}"){
        fun createRoute(startweek: Long,endweek: Long) = "recipeweeklist/${startweek}/${endweek}"
    }

    object ShoppingListScreen: Screens("shoppoingListScreen");
    object IngredientList: Screens("ingredients")
    object CreateShoppingListScreen: Screens("createShoppingListScreen")
    object CreateShoppingListFromTemplateScreen: Screens("createShoppingListFromTemplateScreen/{listID}"){
        fun createRoute(listID:Int) = "createShoppingListFromTemplateScreen/${listID}"
    }
    object EditShoppingListScreen: Screens("shoppingList/{listID}"){
        fun createRoute(listID:Int) = "shoppingList/${listID}"
    }
    object TipScreen: Screens("tipScreen");
    object DayCalorieScreen : Screens("dayCalorieScreen/{day}/{month}/{year}") {
        fun createRoute(day: Int, month: Int, year: Int): String {
            return "dayCalorieScreen/$day/$month/$year"
        }
    }
    object ChangeMacrosScreen: Screens("changeMacrosScreen")
    object HealthScreen: Screens("healthScreen")
    object AddReminderScreen: Screens("addReminderScreen")
}