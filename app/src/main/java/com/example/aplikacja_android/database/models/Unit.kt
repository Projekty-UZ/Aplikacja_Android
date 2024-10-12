package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Units")
data class Unit(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val jednostka:String,
)
