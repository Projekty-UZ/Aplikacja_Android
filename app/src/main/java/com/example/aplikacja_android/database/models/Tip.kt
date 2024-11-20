package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tips")
data class Tip (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ingredient : String,
    val tip : String
)