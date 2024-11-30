package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacja_android.database.models.Activity
import java.time.LocalDate

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: Activity)

    @Delete
    suspend fun delete(activity: Activity)

    @Query("SELECT * FROM activities WHERE date = :date")
    fun getActivitiesByDate(date: LocalDate): LiveData<List<Activity>>
}