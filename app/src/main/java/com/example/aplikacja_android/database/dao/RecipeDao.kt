package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aplikacja_android.database.models.Recipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe)
    @Delete
    suspend fun delete(recipe: Recipe)
    @Update
    suspend fun update(recipe: Recipe)
    @Query("SELECT * FROM Recipes")
    fun getAllRecipes():LiveData<List<Recipe>>
}