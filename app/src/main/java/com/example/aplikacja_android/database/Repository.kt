package com.example.aplikacja_android.database

import androidx.lifecycle.LiveData
import com.example.aplikacja_android.database.dao.CalendarMealDao
import com.example.aplikacja_android.database.dao.IgredientDao
import com.example.aplikacja_android.database.dao.RecipeDao
import com.example.aplikacja_android.database.dao.RecipeIgredientCrossRefDao
import com.example.aplikacja_android.database.dao.ShoppingItemDao
import com.example.aplikacja_android.database.dao.ShoppingListDao
import com.example.aplikacja_android.database.dao.UnitDao
import com.example.aplikacja_android.database.models.CalendarMeal
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef
import com.example.aplikacja_android.database.models.ShoppingItem
import com.example.aplikacja_android.database.models.ShoppingList
import com.example.aplikacja_android.database.models.Unit
import java.time.LocalDate
import java.util.Date

class Repository(
    private val igredientDao: IgredientDao,
    private val recipeDao: RecipeDao,
    private val unitDao: UnitDao,
    private val recipeIgredientCrossRefDao: RecipeIgredientCrossRefDao,
    private val calendarMealDao: CalendarMealDao,
    private val shoppingListDao: ShoppingListDao,
    private val shoppingItemDao: ShoppingItemDao
) {
    // Recipe and Ingredient methods
    val recipes = recipeDao.getAllRecipes()
    val igredients = igredientDao.getAllIgredients()
    val units = unitDao.getAllUnits()
    val shoppingLists = shoppingListDao.getAllShoppingLists()

    suspend fun createMeasurementUnit(unit: Unit) = unitDao.insert(unit)

    fun getIgredientsOfRecipe(id: Int) =
        recipeIgredientCrossRefDao.getAllIgredientsOfRecipeWithUnits(id)

    fun getRecipesWithIgredient(id: Int) = recipeIgredientCrossRefDao.getAllRecipesWithIgredient(id)

    fun getSingleRecipe(id: Int): Recipe = recipeDao.getRecipe(id)
    suspend fun createRecipe(recipe: Recipe): Long = recipeDao.insert(recipe)
    suspend fun updateRecipe(recipe: Recipe) = recipeDao.update(recipe)
    suspend fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipeWithCrossRefs(recipe)

    suspend fun updateIgredient(igredient: Igredient) = igredientDao.update(igredient)

    // RecipeIgredientCrossRef methods
    suspend fun createRecipeIgredientCrossRef(crossRef: RecipeIgredientCrossRef) {
        recipeIgredientCrossRefDao.insert(crossRef)
    }

    suspend fun deleteRecipeIgredientCrossRef(crossRef: RecipeIgredientCrossRef) {
        recipeIgredientCrossRefDao.delete(crossRef)
    }

    // Calendar Meal methods
    suspend fun saveCalendarMeal(calendarMeal: CalendarMeal) =
        calendarMealDao.insertMeal(calendarMeal)

    suspend fun getAllRecipesOfDate(localDate: LocalDate) =
        calendarMealDao.getMealsByDate(localDate)

    suspend fun deleteAllRecipesOfDate(localDate: LocalDate) =
        calendarMealDao.deleteAllRecipesFromDate(localDate)

    suspend fun getMealsByDateRange(startDate: LocalDate, endDate: LocalDate): List<CalendarMeal> {
        return calendarMealDao.getMealsByDateRange(startDate, endDate)
    }

    // Shopping List methods
    suspend fun updateShoppingList(shoppingList: ShoppingList) = shoppingListDao.updateShoppingList(shoppingList)
    suspend fun createShoppingList(shoppingList: ShoppingList): Long =
        shoppingListDao.insertShoppingList(shoppingList)

    suspend fun getShoppingListByDate(date: LocalDate): ShoppingList? =
        shoppingListDao.getShoppingListByDate(date)


    suspend fun deleteShoppingList(shoppingList: ShoppingList) =
        shoppingListDao.deleteShoppingList(shoppingList)

    // Shopping Item methods
    suspend fun createShoppingItem(shoppingItem: ShoppingItem): Long =
        shoppingItemDao.insertShoppingItem(shoppingItem)

    fun getItemsForShoppingList(listId: Int): LiveData<List<ShoppingItem>> =
        shoppingItemDao.getItemsByListId(listId)

    suspend fun updateItemBoughtStatus(itemId: Int, isBought: Boolean) =
        shoppingItemDao.updateItemBoughtStatus(itemId, isBought)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) =
        shoppingItemDao.deleteShoppingItem(shoppingItem)

    // Metoda do aktualizacji ilości składnika na liście zakupów
    suspend fun updateShoppingItemQuantity(itemId: Int, quantity: Int) {
        shoppingItemDao.updateShoppingItemQuantity(itemId, quantity)
    }

    // Metoda do oznaczania składnika jako dostępnego w domu
    suspend fun updateIngredientAvailability(ingredientId: Int, isAvailableAtHome: Boolean) {
        igredientDao.updateIngredientAvailability(ingredientId, isAvailableAtHome)
    }
}