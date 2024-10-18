package com.example.aplikacja_android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacja_android.database.models.CalendarMeal
import java.time.LocalDate
import java.util.Date

@Dao
interface CalendarMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(calendarMeal: CalendarMeal)

    @Query("DELETE FROM CalendarMeals WHERE recipeDate = :localDate")
    suspend fun deleteAllRecipesFromDate(localDate: LocalDate)

    @Query("SELECT * FROM CalendarMeals WHERE recipeDate = :localDate")
    suspend fun getMealsByDate(localDate: LocalDate): List<CalendarMeal>

    @Query("SELECT * FROM CalendarMeals")
    suspend fun getAllMeals(): List<CalendarMeal>
}