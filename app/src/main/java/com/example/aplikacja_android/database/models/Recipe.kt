package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName="Recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val nazwa:String,
    val instrukcja:String,
    val rodzaj:String,
    val isFavorite:Boolean
)