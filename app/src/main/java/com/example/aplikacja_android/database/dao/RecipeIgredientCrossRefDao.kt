package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef

@Dao
interface RecipeIgredientCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: RecipeIgredientCrossRef)
    @Delete
    suspend fun delete(crossRef: RecipeIgredientCrossRef)
    @Update
    suspend fun update(crossRef: RecipeIgredientCrossRef)

    @Query("SELECT Igredients.*,Units.jednostka,RecipeIgredientCrossRef.ilosc FROM Igredients " +
            "INNER JOIN RecipeIgredientCrossRef ON Igredients.id = RecipeIgredientCrossRef.igredientId " +
            "INNER JOIN Units ON Units.id = RecipeIgredientCrossRef.unitId " +
            "WHERE RecipeIgredientCrossRef.recipeId = :id ")
    fun getAllIgredientsOfRecipeWithUnits(id:Int):LiveData<List<IngredientWithUnit>>

    @Query("SELECT Recipes.* FROM Recipes " +
            "INNER JOIN RecipeIgredientCrossRef ON Recipes.id = RecipeIgredientCrossRef.recipeId " +
            "WHERE RecipeIgredientCrossRef.igredientId = :id")
    fun getAllRecipesWithIgredient(id:Int):LiveData<List<Recipe>>
}

data class IngredientWithUnit(
    @Embedded val ingredient: Igredient,
    val jednostka: String, // Unit type (e.g., g, ml, etc.)
    val ilosc: Int // Quantity of the ingredient
)