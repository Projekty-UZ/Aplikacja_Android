package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacja_android.database.models.Macros

@Dao
interface MacrosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(macros: Macros)

    @Query("SELECT * FROM Macros WHERE id = 0")
    fun getMacros(): LiveData<Macros>
}