package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacja_android.database.models.BloodSugarMeasurement
import java.time.LocalDate

@Dao
interface BloodSugarMeasurmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(measurement: BloodSugarMeasurement)

    @Query("SELECT * FROM blood_sugar_measurements WHERE date = :date")
    fun getMeasurementsByDate(date: LocalDate): LiveData<BloodSugarMeasurement>
}