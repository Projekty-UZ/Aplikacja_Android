package com.example.aplikacja_android.database

import androidx.lifecycle.LiveData
import com.example.aplikacja_android.database.dao.ActivityDao
import com.example.aplikacja_android.database.dao.ActivityTypeDao
import com.example.aplikacja_android.database.dao.BloodPressureMeasurmentsDao
import com.example.aplikacja_android.database.dao.BloodSugarMeasurmentDao
import com.example.aplikacja_android.database.dao.BodyMeasurementsDao
import com.example.aplikacja_android.database.dao.CalendarMealDao
import com.example.aplikacja_android.database.dao.DailyWeightDao
import com.example.aplikacja_android.database.dao.IgredientDao
import com.example.aplikacja_android.database.dao.MacrosDao
import com.example.aplikacja_android.database.dao.NoteDao
import com.example.aplikacja_android.database.dao.RecipeDao
import com.example.aplikacja_android.database.dao.RecipeIgredientCrossRefDao
import com.example.aplikacja_android.database.dao.ShoppingItemDao
import com.example.aplikacja_android.database.dao.ShoppingListDao
import com.example.aplikacja_android.database.dao.TipDao
import com.example.aplikacja_android.database.dao.UnitDao
import com.example.aplikacja_android.database.models.Activity
import com.example.aplikacja_android.database.models.BloodPressureMeasurement
import com.example.aplikacja_android.database.models.BloodSugarMeasurement
import com.example.aplikacja_android.database.models.BodyMeasurements
import com.example.aplikacja_android.database.models.CalendarMeal
import com.example.aplikacja_android.database.models.DailyWeight
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Macros
import com.example.aplikacja_android.database.models.Note
import com.example.aplikacja_android.database.models.Unit
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef
import com.example.aplikacja_android.database.models.ShoppingItem
import com.example.aplikacja_android.database.models.ShoppingList
import java.time.LocalDate

class Repository(
    private val igredientDao: IgredientDao,
    private val recipeDao: RecipeDao,
    private val unitDao: UnitDao,
    private val recipeIgredientCrossRefDao: RecipeIgredientCrossRefDao,
    private val calendarMealDao: CalendarMealDao,
    private val shoppingListDao: ShoppingListDao,
    private val shoppingItemDao: ShoppingItemDao,
    private val tipDao: TipDao,
    private val noteDao: NoteDao,
    private val macrosDao: MacrosDao,
    private val activityDao: ActivityDao,
    private val activityTypeDao: ActivityTypeDao,
    private val bodyMeasurementsDao: BodyMeasurementsDao,
    private val bloodSugarMeasurementDao: BloodSugarMeasurmentDao,
    private val bloodPressureMeasurmentsDao: BloodPressureMeasurmentsDao,
    private val dailyWeightDao: DailyWeightDao
) {
    // Recipe and Ingredient methods
    val recipes = recipeDao.getAllRecipes()
    val igredients = igredientDao.getAllIgredients()
    val units = unitDao.getAllUnits()
    val shoppingLists = shoppingListDao.getAllShoppingLists()
    val tips = tipDao.getAllTips()
    val macros = macrosDao.getMacros()
    val bodyMeasurements = bodyMeasurementsDao.getBodyMeasurements()
    val activityTypes = activityTypeDao.getAllActivityType()

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

    fun  getRecipesOfDate(localDate: LocalDate) =
        calendarMealDao.getRecipesForDate(localDate)

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

    //note methods
    suspend fun createNote(note: Note)=
        noteDao.insertNote(note)

    suspend fun deleteNote(note: Note)=
        noteDao.deleteNote(note)

    fun getNotesByRecipeId(recipeId: Int): LiveData<List<Note>> {
        return noteDao.getNotesByRecipeId(recipeId)
    }
    //macros methods
    suspend fun updateMacros(macros: Macros) = macrosDao.insertOrUpdate(macros)
    //body measurements methods
    suspend fun updateBodyMeasurements(bodyMeasurements: BodyMeasurements) = bodyMeasurementsDao.insert(bodyMeasurements)
    //blood sugar measurements methods
    suspend fun createBloodSugarMeasurement(bloodSugarMeasurement: BloodSugarMeasurement) = bloodSugarMeasurementDao.insert(bloodSugarMeasurement)

    fun getBloodSugarMeasurementByDate(date: LocalDate): LiveData<BloodSugarMeasurement> {
        return bloodSugarMeasurementDao.getMeasurementsByDate(date)
    }

    //blood pressure measurements methods
    suspend fun createBloodPressureMeasurement(bloodPressureMeasurement: BloodPressureMeasurement) = bloodPressureMeasurmentsDao.insert(bloodPressureMeasurement)

    fun getBloodPressureMeasurementByDate(date: LocalDate): LiveData<BloodPressureMeasurement> {
        return bloodPressureMeasurmentsDao.getMeasurementsByDate(date)
    }

    //activity methods
    suspend fun createActivity(activity: Activity) = activityDao.insert(activity)
    suspend fun deleteActivity(activity: Activity) = activityDao.delete(activity)
    fun getActivitiesByDate(date: LocalDate): LiveData<List<Activity>> {
        return activityDao.getActivitiesByDate(date)
    }
    //daily weight methods
    suspend fun createDailyWeight(dailyWeight: DailyWeight) = dailyWeightDao.insertDailyWeight(dailyWeight)
    fun getDailyWeightByDate(date: LocalDate): LiveData<DailyWeight> {
        return dailyWeightDao.getDailyWeightByDate(date)
    }
    fun getLastDailyWeight(): LiveData<DailyWeight> {
        return dailyWeightDao.getLastDailyWeight()
    }



}