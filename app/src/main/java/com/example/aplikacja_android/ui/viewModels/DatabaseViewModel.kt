package com.example.aplikacja_android.ui.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.aplikacja_android.database.Repository
import com.example.aplikacja_android.database.dao.IngredientWithUnit
import com.example.aplikacja_android.database.models.CalendarMeal
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef
import com.example.aplikacja_android.database.models.ShoppingItem
import com.example.aplikacja_android.database.models.ShoppingList
import com.example.aplikacja_android.database.models.Tip
import com.example.aplikacja_android.database.models.Unit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date

class DatabaseViewModel(private val repository:Repository):ViewModel() {
    val allIgredients:LiveData<List<Igredient>> = repository.igredients
    val allRecipes: LiveData<List<Recipe>> = repository.recipes
    val allUnits: LiveData<List<Unit>> = repository.units
    val allShoppingList: LiveData<List<ShoppingList>> = repository.shoppingLists
    val allTips: LiveData<List<Tip>> = repository.tips

    val selectedRecipe = MutableLiveData<Recipe?>()
    val selectedList = MutableLiveData<ShoppingList?>()



    fun selectRecipe(recipe: Recipe){
        selectedRecipe.value = recipe
    }

    fun selectShoppingList(shoppingList: ShoppingList){
        selectedList.value = shoppingList
    }

    suspend fun saveRecipe(recipe: Recipe):Long {
        return repository.createRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe){
        repository.updateRecipe(recipe)
    }

    suspend fun deleteRecipesWithCrossRef(recipe: Recipe){
        repository.deleteRecipe(recipe)
    }

    fun getSingleRecipe(id: Int): Recipe {
        return repository.getSingleRecipe(id)
    }

    fun getIngredientsOfRecipe(id: Int): LiveData<List<IngredientWithUnit>> {
        return repository.getIgredientsOfRecipe(id)
    }

    fun getRecipesWithIngredient(id: Int): LiveData<List<Recipe>>{
        return repository.getRecipesWithIgredient(id)
    }

    suspend fun updateIgredient(igredient: Igredient){
        repository.updateIgredient(igredient)
    }

    suspend fun deleteCrossRef(recipeIgredientCrossRef: RecipeIgredientCrossRef){
        repository.deleteRecipeIgredientCrossRef(recipeIgredientCrossRef)
    }

    suspend fun saveCrossRef(recipeIgredientCrossRef: RecipeIgredientCrossRef){
        repository.createRecipeIgredientCrossRef(recipeIgredientCrossRef)
    }

    suspend fun getAllRecipesOfDate(localDate: LocalDate): List<CalendarMeal>{
        return repository.getAllRecipesOfDate(localDate)
    }

    suspend fun deleteAllRecipesOfDate(localDate: LocalDate){
        return repository.deleteAllRecipesOfDate(localDate)
    }

    suspend fun insertCalendarMeal(calendarMeal: CalendarMeal){
        return repository.saveCalendarMeal(calendarMeal)
    }
    // Shopping List methods

    suspend fun updateShoppingList(shoppingList: ShoppingList){
        repository.updateShoppingList(shoppingList)
    }
    suspend fun createShoppingList(shoppingList: ShoppingList): Long {
        return repository.createShoppingList(shoppingList)
    }

    suspend fun getShoppingListByDate(date: LocalDate): ShoppingList? {
        return repository.getShoppingListByDate(date)
    }

    suspend fun deleteShoppingList(shoppingList: ShoppingList) {
        repository.deleteShoppingList(shoppingList)
    }

    // Shopping Item methods
    fun getItemsForShoppingList(listId: Int): LiveData<List<ShoppingItem>> {
        return repository.getItemsForShoppingList(listId)
    }

    suspend fun updateItemBoughtStatus(itemId: Int, isBought: Boolean) {
        repository.updateItemBoughtStatus(itemId, isBought)
    }

    suspend fun createShoppingItem(shoppingItem: ShoppingItem): Long {
        return repository.createShoppingItem(shoppingItem)
    }

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        repository.deleteShoppingItem(shoppingItem)
    }

    // Update ingredient's quantity on the shopping list
    suspend fun updateShoppingItemQuantity(itemId: Int, quantity: Int) {
        repository.updateShoppingItemQuantity(itemId, quantity)
    }

    // Mark ingredient as available at home
    suspend fun updateIngredientAvailability(ingredientId: Int, isAvailableAtHome: Boolean) {
        repository.updateIngredientAvailability(ingredientId, isAvailableAtHome)
    }
    // Generate shopping list based on planned meals for a date range
    @RequiresApi(Build.VERSION_CODES.O)
    fun generateShoppingListForDateRange(startDate: LocalDate, endDate: LocalDate) {
        GlobalScope.launch {
            val meals = repository.getMealsByDateRange(startDate, endDate)
            val ingredients = mutableMapOf<Igredient, Int>()

            meals.forEach { meal ->
                val recipeIngredients = repository.getIgredientsOfRecipe(meal.recipeId).asFlow().firstOrNull() ?: emptyList()
                recipeIngredients.forEach { ingredientWithUnit ->
                    ingredients[ingredientWithUnit.ingredient] =
                        ingredients.getOrDefault(ingredientWithUnit.ingredient, 0) + ingredientWithUnit.ilosc
                }
            }

            // Creating a ShoppingList based on collected ingredients and saving it to the database
            val shoppingList = ShoppingList(
                name = "Shopping List from $startDate to $endDate",
                date = LocalDate.now()
            )
            val shoppingListId = repository.createShoppingList(shoppingList)

            ingredients.forEach { (ingredient, quantity) ->
                if (!ingredient.isAvailableAtHome) { // Check availability at home
                    val shoppingItem = ShoppingItem(
                        listId = shoppingListId.toInt(),
                        name = ingredient.nazwa,
                        quantity = quantity,
                        kategoria = ingredient.kategoria
                    )
                    repository.createShoppingItem(shoppingItem)
                }
            }
        }
    }
}
class DatabaseViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DatabaseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

val LocalDatabaseViewModel = staticCompositionLocalOf<DatabaseViewModel> {
    error("No DatabaseViewModel provided")
}