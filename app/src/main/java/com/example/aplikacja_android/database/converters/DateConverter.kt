package com.example.aplikacja_android.database.converters

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class DateConverter {
    @SuppressLint("NewApi")
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let {
            Date(it).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }

    @SuppressLint("NewApi")
    @TypeConverter
    fun localDateToTimestamp(date: LocalDate?): Long? {
        return date?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
}