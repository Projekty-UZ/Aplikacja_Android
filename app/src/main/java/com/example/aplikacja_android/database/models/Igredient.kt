package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Igredients")
data class Igredient(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val nazwa:String,
)