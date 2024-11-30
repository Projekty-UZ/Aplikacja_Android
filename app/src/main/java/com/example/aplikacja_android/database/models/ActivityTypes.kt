package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_types")
data class ActivityType(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val caloriesPerMinute: Double
)