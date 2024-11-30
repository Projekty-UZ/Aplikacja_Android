package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "daily_weights")
data class DailyWeight(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val weight: Double,
    val date: LocalDate
    )
