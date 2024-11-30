package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "blood_sugar_measurements")
data class BloodSugarMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val sugarLevel: Int
)