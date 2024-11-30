package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val durationInMinutes: Int,
    val totalCaloriesBurned: Double,
    val activityTypeId: Int
)
