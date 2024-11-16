package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "ShoppingLists")
data class ShoppingList(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "Shopping List", // Dodano nazwę listy
    val date: LocalDate,
    val isTemplate: Boolean = false // Flaga dla list zapisanych jako szablon
)
