package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacja_android.database.models.BodyMeasurements

@Dao
interface BodyMeasurementsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(measurement: BodyMeasurements)

    @Query("SELECT * FROM body_measurements WHERE id = 0")
    fun getBodyMeasurements(): LiveData<BodyMeasurements>
}