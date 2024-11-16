package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aplikacja_android.database.models.Igredient

@Dao
interface IgredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(igredient: Igredient)
    @Delete
    suspend fun delete(igredient: Igredient)
    @Update
    suspend fun update(igredient: Igredient)
    @Query("SELECT * FROM Igredients")
    fun getAllIgredients(): LiveData<List<Igredient>>
    @Query("UPDATE Igredients SET isAvailableAtHome = :isAvailable WHERE id = :ingredientId")
    suspend fun updateIngredientAvailability(ingredientId: Int, isAvailable: Boolean)
}