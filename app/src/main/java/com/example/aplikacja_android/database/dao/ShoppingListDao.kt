package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.example.aplikacja_android.database.models.ShoppingList
import java.time.LocalDate

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingList(shoppingList: ShoppingList): Long

    @Query("SELECT * FROM ShoppingLists WHERE date = :date")
    suspend fun getShoppingListByDate(date: LocalDate): ShoppingList?

    @Query("SELECT * FROM ShoppingLists")
    fun getAllShoppingLists(): LiveData<List<ShoppingList>>

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    @Delete
    suspend fun deleteShoppingList(shoppingList: ShoppingList)

    @Query("SELECT * FROM ShoppingLists WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getShoppingListsByDateRange(startDate: LocalDate, endDate: LocalDate): List<ShoppingList>

    @Query("SELECT * FROM ShoppingLists ORDER BY date DESC LIMIT :limit")
    suspend fun getRecentShoppingLists(limit: Int): List<ShoppingList>
}
