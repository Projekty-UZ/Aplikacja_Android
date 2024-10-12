package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "RecipeIgredientCrossRef",
    primaryKeys = ["recipeId","igredientId","unitId"],
    foreignKeys = [
        ForeignKey(entity = Recipe::class, parentColumns = ["id"], childColumns = ["recipeId"], onDelete = CASCADE),
        ForeignKey(entity = Igredient::class, parentColumns = ["id"], childColumns = ["igredientId"], onDelete = CASCADE),
        ForeignKey(entity = Unit::class, parentColumns = ["id"], childColumns = ["unitId"], onDelete = CASCADE),
    ]
    )
data class RecipeIgredientCrossRef(
    val recipeId:Int,
    val igredientId:Int,
    val unitId:Int,
    val ilosc:Int
)
