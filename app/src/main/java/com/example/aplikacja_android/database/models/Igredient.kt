package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Igredients")
data class Igredient(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val nazwa:String,
    val kategoria: String,
    val isAvailableAtHome: Boolean = false,
    val kalorie : Double,
    val bialko: Double,
    val tluszcz: Double,
    val weglowodany: Double
    )