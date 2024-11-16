package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aplikacja_android.database.models.ShoppingItem

@Dao
interface ShoppingItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem): Long

    @Query("SELECT * FROM ShoppingItems WHERE listId = :listId")
    fun getItemsByListId(listId: Int): LiveData<List<ShoppingItem>>

    @Update
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    @Query("UPDATE ShoppingItems SET isBought = :isBought WHERE itemId = :itemId")
    suspend fun updateItemBoughtStatus(itemId: Int, isBought: Boolean)

    // Nowa metoda do aktualizacji ilości składnika na liście zakupów
    @Query("UPDATE ShoppingItems SET quantity = :quantity WHERE itemId = :itemId")
    suspend fun updateShoppingItemQuantity(itemId: Int, quantity: Int)
}
