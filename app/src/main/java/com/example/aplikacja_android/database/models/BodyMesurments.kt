package com.example.aplikacja_android.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_measurements")
data class BodyMeasurements(
    @PrimaryKey val id: Int = 0,
    val height: Double,
    val waist: Double,   
    val hip: Double,      
    val chest: Double,    
    val thigh: Double,  
    val neck: Double,      
    val bicep: Double ,
    val desiredWeight: Double
)