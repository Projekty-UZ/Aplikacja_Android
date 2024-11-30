package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacja_android.database.models.DailyWeight
import java.time.LocalDate

@Dao
interface DailyWeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyWeight(dailyWeight: DailyWeight)

    @Query("SELECT * FROM daily_weights WHERE date = :date")
    fun getDailyWeightByDate(date: LocalDate): LiveData<DailyWeight>

    @Query("SELECT * FROM daily_weights ORDER BY date DESC LIMIT 1")
    fun getLastDailyWeight(): LiveData<DailyWeight>

    @Query("SELECT * FROM daily_weights WHERE date BETWEEN :startDate AND :endDate ORDER BY date")
    fun getDailyWeightsForMonth(startDate: LocalDate, endDate: LocalDate): LiveData<List<DailyWeight>>

}