package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "blood_pressure_measurements")
data class BloodPressureMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int? = null
)