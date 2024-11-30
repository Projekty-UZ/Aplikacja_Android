package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.aplikacja_android.database.models.ActivityType

@Dao
interface ActivityTypeDao {
    @Insert
    suspend fun insert(activityType: ActivityType)

    @Query("SELECT * FROM activity_types")
    fun getAllActivityType(): LiveData<List<ActivityType>>
}