package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Macros")
data class Macros(
    @PrimaryKey val id: Int = 0,
    var calories: Double,
    val protein: Double,
    val fat: Double,
    val carbohydrates: Double
)
