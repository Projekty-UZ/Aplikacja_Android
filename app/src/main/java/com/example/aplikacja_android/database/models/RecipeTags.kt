package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_tags")
data class RecipeTags(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val recipeId: Int
)
