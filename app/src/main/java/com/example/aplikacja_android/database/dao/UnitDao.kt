package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aplikacja_android.database.models.Unit

@Dao
interface UnitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(unit: Unit)
    @Delete
    suspend fun delete(unit: Unit)
    @Update
    suspend fun update(unit: Unit)
    @Query("SELECT * FROM Units")
    fun getAllUnits(): LiveData<List<Unit>>
}