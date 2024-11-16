package com.example.aplikacja_android

import android.app.Application
import com.example.aplikacja_android.database.AppDatabase
import com.example.aplikacja_android.database.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AndroidApp : Application(){
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { Repository(database.igredientDao()
        ,database.recipeDao()
        ,database.unitDao()
        ,database.recipeIgredientCrossRefDao(),
        database.calendarMealDao(),
        database.shoppingListDao(),
        database.shoppingItemDao()) }
}