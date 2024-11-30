package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacja_android.database.models.CalendarMeal
import com.example.aplikacja_android.database.models.Recipe
import java.time.LocalDate

@Dao
interface CalendarMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(calendarMeal: CalendarMeal)

    @Query("DELETE FROM CalendarMeals WHERE recipeDate = :localDate")
    suspend fun deleteAllRecipesFromDate(localDate: LocalDate)

    @Query("SELECT * FROM CalendarMeals WHERE recipeDate = :localDate")
    suspend fun getMealsByDate(localDate: LocalDate): List<CalendarMeal>

    @Query("""
        SELECT Recipes.id, Recipes.instrukcja, Recipes.nazwa, Recipes.rodzaj FROM Recipes 
        INNER JOIN CalendarMeals ON Recipes.id = CalendarMeals.recipeId
        WHERE CalendarMeals.recipeDate = :localDate
    """)
    fun getRecipesForDate(localDate: LocalDate): LiveData<List<Recipe>>

    @Query("SELECT * FROM CalendarMeals")
    suspend fun getAllMeals(): List<CalendarMeal>

    @Query("SELECT * FROM CalendarMeals WHERE recipeDate BETWEEN :startDate AND :endDate")
    suspend fun getMealsByDateRange(startDate: LocalDate, endDate: LocalDate): List<CalendarMeal>
}