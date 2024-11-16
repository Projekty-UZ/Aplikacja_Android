package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ShoppingItems")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val itemId: Int = 0,
    val listId: Int,
    val name: String,
    val quantity: Int,
    val isBought: Boolean = false,
    val kategoria: String = ""
)
