package com.example.aplikacja_android.ui.viewModels

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.aplikacja_android.Notifications.scheduleReminder
import com.example.aplikacja_android.database.Repository
import com.example.aplikacja_android.database.dao.IngredientWithUnit
import com.example.aplikacja_android.database.models.Activity
import com.example.aplikacja_android.database.models.ActivityType
import com.example.aplikacja_android.database.models.BloodPressureMeasurement
import com.example.aplikacja_android.database.models.BloodSugarMeasurement
import com.example.aplikacja_android.database.models.BodyMeasurements
import com.example.aplikacja_android.database.models.CalendarMeal
import com.example.aplikacja_android.database.models.DailyWeight
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Macros
import com.example.aplikacja_android.database.models.Note
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.Unit
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef
import com.example.aplikacja_android.database.models.RecipeTags
import com.example.aplikacja_android.database.models.Reminder
import com.example.aplikacja_android.database.models.ShoppingItem
import com.example.aplikacja_android.database.models.ShoppingList
import com.example.aplikacja_android.database.models.Tip
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

class DatabaseViewModel(private val repository:Repository):ViewModel() {
    val allIgredients:LiveData<List<Igredient>> = repository.igredients
    val allRecipes: LiveData<List<Recipe>> = repository.recipes
    val allUnits: LiveData<List<Unit>> = repository.units
    val allShoppingList: LiveData<List<ShoppingList>> = repository.shoppingLists
    val allTips: LiveData<List<Tip>> = repository.tips
    val allActivityTypes: LiveData<List<ActivityType>> = repository.activityTypes

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

    fun getFavoriteRecipes(): LiveData<List<Recipe>> {
        return repository.getFavoriteRecipes()
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

    fun getRecipesForDate(localDate: LocalDate): LiveData<List<Recipe>>{
        return repository.getRecipesOfDate(localDate)
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
    //note methods
    suspend fun insertNoteForRecipe(note: Note){
        repository.createNote(note)
    }
    suspend fun deleteNoteForRecipe(note: Note){
        repository.deleteNote(note)
    }
    fun getNotesForRecipe(recipeId: Int): LiveData<List<Note>>{
        return repository.getNotesByRecipeId(recipeId)
    }
    //macros methods
    fun getMacros(): LiveData<Macros>{
        return repository.macros
    }
    suspend fun updateMacros(macros: Macros){
        repository.updateMacros(macros)
    }
    //body measurements methods
    fun getBodyMeasurements(): LiveData<BodyMeasurements>{
        return repository.bodyMeasurements
    }
    suspend fun updateBodyMeasurements(bodyMeasurements: BodyMeasurements){
        repository.updateBodyMeasurements(bodyMeasurements)
    }
    //activity methods
    suspend fun insertActivity(activity: Activity){
        repository.createActivity(activity)
    }
    fun getActivitiesOfDate(localDate: LocalDate): LiveData<List<Activity>>{
        return repository.getActivitiesByDate(localDate)
    }
    suspend fun deleteActivity(activity: Activity){
        repository.deleteActivity(activity)
    }

    fun calculateCaloriesBurnedForMonth(): LiveData<Double>{
        val localDate = LocalDate.now()
        var caloriesBurnedTotal = MutableLiveData<Double>()

        viewModelScope.launch {
            val allActivities = mutableListOf<Activity>()
            for(i in 1..localDate.lengthOfMonth()){
                val date = LocalDate.of(localDate.year, localDate.month, i)
                val activities = getActivitiesOfDate(date).asFlow().firstOrNull() ?: emptyList()
                if(activities.isNotEmpty()){
                    allActivities.addAll(activities)
                }
            }
            caloriesBurnedTotal.value = sumCaloriesBurnedForDay(allActivities)
        }
        return caloriesBurnedTotal
    }

    fun sumCaloriesBurnedForDay(activities: List<Activity>): Double{
        var totalCalories = 0.0
        activities.forEach { activity ->
            totalCalories += activity.totalCaloriesBurned
        }
        return totalCalories
    }

    //blood sugar measurements methods
    suspend fun insertBloodSugarMeasurement(bloodSugarMeasurement: BloodSugarMeasurement){
        repository.createBloodSugarMeasurement(bloodSugarMeasurement)
    }
    fun getBloodSugarMeasurementByDate(date: LocalDate): LiveData<BloodSugarMeasurement>{
        return repository.getBloodSugarMeasurementByDate(date)
    }

    //blood pressure measurements methods
    suspend fun insertBloodPressureMeasurement(bloodPressureMeasurement: BloodPressureMeasurement){
        repository.createBloodPressureMeasurement(bloodPressureMeasurement)
    }
    fun getBloodPressureMeasurementByDate(date: LocalDate): LiveData<BloodPressureMeasurement>{
        return repository.getBloodPressureMeasurementByDate(date)
    }
    //daily weight methods
    suspend fun insertDailyWeight(dailyWeight: DailyWeight){
        repository.createDailyWeight(dailyWeight)
    }
    fun getDailyWeightByDate(date: LocalDate): LiveData<DailyWeight>{
        return repository.getDailyWeightByDate(date)
    }
    fun getLastDailyWeight(): LiveData<DailyWeight>{
        return repository.getLastDailyWeight()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeightsForCurrentMonth(): LiveData<List<DailyWeight>> {
        val currentDate = LocalDate.now()
        val firstDayOfMonth = currentDate.withDayOfMonth(1)
        val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())

        return repository.getDailyWeightsForMonth(firstDayOfMonth, lastDayOfMonth)
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

    fun sumNutrientsFromIngredients(ingredients: List<IngredientWithUnit>): Map<String, Double> {
        val unitConversion = mapOf(
            "g" to 1.0,
            "ml" to 1.0,
            "lyzeczka" to 5.0,
            "lyzka" to 10.0
        )

        var totalKalorie = 0.0
        var totalBialko = 0.0
        var totalTluszcz = 0.0
        var totalWeglowodany = 0.0

        // Loop through all ingredients and sum up their nutrients
        ingredients.forEach { ingredient ->
            val weightInGrams = ingredient.ilosc * (unitConversion[ingredient.unit.jednostka] ?: 1.0)
            val ingredientDetails = ingredient.ingredient

            // Summing up the nutrients for each ingredient
            totalKalorie += ingredientDetails.kalorie * (weightInGrams / 100.0)
            totalBialko += ingredientDetails.bialko * (weightInGrams / 100.0)
            totalTluszcz += ingredientDetails.tluszcz * (weightInGrams / 100.0)
            totalWeglowodany += ingredientDetails.weglowodany * (weightInGrams / 100.0)
        }

        return mapOf(
            "kalorie" to totalKalorie,
            "bialko" to totalBialko,
            "tluszcz" to totalTluszcz,
            "weglowodany" to totalWeglowodany
        )
    }

    fun calculateDayNutrients(localDate: LocalDate): LiveData<Map<String, Double>>{
        val nutrientValues = MutableLiveData<Map<String, Double>>()

        viewModelScope.launch {
            val recipes = getRecipesForDate(localDate).asFlow().firstOrNull() ?: emptyList()
            if (recipes.isNotEmpty()) {
                val allIngredients = mutableListOf<IngredientWithUnit>()

                recipes.forEach { recipe ->
                    val ingredients = getIngredientsOfRecipe(recipe.id).asFlow().firstOrNull() ?: emptyList()
                    allIngredients.addAll(ingredients)
                }

                // Now sum the nutrients
                nutrientValues.value = sumNutrientsFromIngredients(allIngredients)
            }
        }

        return nutrientValues
    }

    fun calculateCurrentMonthNutrients(): LiveData<Map<String, Double>> {
        val nutrientValues = MutableLiveData<Map<String, Double>>()

        viewModelScope.launch {
            val currentDate = LocalDate.now()
            val allIngredients = mutableListOf<IngredientWithUnit>()
            for(i in 1..currentDate.lengthOfMonth()){
                val date = LocalDate.of(currentDate.year, currentDate.month, i)
                val recipes = getRecipesForDate(date).asFlow().firstOrNull() ?: emptyList()
                if (recipes.isNotEmpty()) {

                    recipes.forEach { recipe ->
                        val ingredients = getIngredientsOfRecipe(recipe.id).asFlow().firstOrNull() ?: emptyList()
                        allIngredients.addAll(ingredients)
                    }
                }
            }
            nutrientValues.value = sumNutrientsFromIngredients(allIngredients)
        }

        return nutrientValues
    }

    fun calculateDesiredCurrentMonthNutrients(): LiveData<Map<String, Double>>{
        val desiredNutrientsForDay = MutableLiveData<Map<String, Double>>()
        var daysWithRecipes by mutableStateOf(0)
        viewModelScope.launch {
            var desiredNutrients = getMacros().asFlow().firstOrNull() ?: Macros(0,0.0, 0.0, 0.0,0.0)
            val currentDate = LocalDate.now()

            for(i in 1..currentDate.lengthOfMonth()){
                val date = LocalDate.of(currentDate.year, currentDate.month, i)
                val recipes = getRecipesForDate(date).asFlow().firstOrNull() ?: emptyList()
                if (recipes.isNotEmpty()) {
                    daysWithRecipes++
                }
            }
            desiredNutrientsForDay.value = mapOf(
                "kalorie" to desiredNutrients.calories * daysWithRecipes,
                "bialko" to desiredNutrients.protein * daysWithRecipes,
                "tluszcz" to desiredNutrients.fat * daysWithRecipes,
                "weglowodany" to desiredNutrients.carbohydrates * daysWithRecipes
            )
        }
        return desiredNutrientsForDay
    }

    fun calculateRecipeNutrients(recipeId: Int): LiveData<Map<String, Double>>{
        val nutrientValues = MutableLiveData<Map<String, Double>>()
        val allIngredients = mutableListOf<IngredientWithUnit>()
        getIngredientsOfRecipe(recipeId).observeForever { ingredients ->
            if (ingredients != null) {
                allIngredients.addAll(ingredients)
            }
            nutrientValues.value = sumNutrientsFromIngredients(allIngredients)
        }
        return nutrientValues
    }
    //recipe tags methods
    fun getTagsByRecipeId(recipeId: Int): LiveData<List<RecipeTags>>{
        return repository.getTagsByRecipeId(recipeId)
    }
    suspend fun insertTag(tag: RecipeTags){
        repository.addTag(tag)
    }
    suspend fun deleteTag(tag: RecipeTags) {
        repository.deleteTag(tag)
    }

    // Reminder methods
    suspend fun createReminder(reminder: Reminder): Reminder? {
        val id = repository.createReminder(reminder)
        return repository.getReminderById(id)
    }

    suspend fun getRemindersForDay(day: String): List<Reminder> {
        return repository.getRemindersForDay(day)
    }

    fun scheduleReminder(context: Context, reminder: Reminder) {
        viewModelScope.launch {
            val delayInMinutes = calculateDelay(reminder.time)
            if (delayInMinutes != null) {

                scheduleReminder(
                    context,
                    reminder.title,
                    reminder.description,
                    delayInMinutes
                )
            }
        }
    }

    // Calculates delay in minutes from the current time
    private fun calculateDelay(time: String): Long? {
        val currentTime = LocalTime.now()
        val reminderTime = LocalTime.parse(time) // e.g., "08:30"

        val minutesUntilReminder = java.time.Duration.between(currentTime, reminderTime).toMinutes()
        return if (minutesUntilReminder > 0) minutesUntilReminder else null
    }

    // Returns the current day of the week as a string (e.g., "Monday")
    private fun getCurrentDayOfWeek(): String {
        return LocalDate.now()
            .dayOfWeek
            .getDisplayName(TextStyle.FULL, Locale.ENGLISH) // e.g., "Monday"
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