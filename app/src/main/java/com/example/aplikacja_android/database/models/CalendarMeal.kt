package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
@Entity(tableName = "CalendarMeals")
data class CalendarMeal (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeDate: LocalDate,
    val recipeId: Int,
    val mealType: String
)