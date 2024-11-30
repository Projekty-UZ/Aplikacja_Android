package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacja_android.database.models.BloodPressureMeasurement
import java.time.LocalDate

@Dao
interface BloodPressureMeasurmentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(measurement: BloodPressureMeasurement)

    @Delete
    suspend fun delete(measurement: BloodPressureMeasurement)

    @Query("SELECT * FROM blood_pressure_measurements WHERE date = :date")
    fun getMeasurementsByDate(date: LocalDate): LiveData<BloodPressureMeasurement>
}