package com.example.aplikacja_android.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aplikacja_android.database.dao.IgredientDao
import com.example.aplikacja_android.database.dao.RecipeDao
import com.example.aplikacja_android.database.dao.RecipeIgredientCrossRefDao
import com.example.aplikacja_android.database.dao.UnitDao
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef
import com.example.aplikacja_android.database.models.Unit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Recipe::class, Igredient::class, RecipeIgredientCrossRef::class,Unit::class], version = 1, exportSchema = false)
abstract  class AppDatabase: RoomDatabase(){
    abstract fun recipeDao(): RecipeDao
    abstract fun igredientDao(): IgredientDao
    abstract fun recipeIgredientCrossRefDao(): RecipeIgredientCrossRefDao
    abstract fun unitDao(): UnitDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context,scope: CoroutineScope): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).addCallback(databaseCallback()).build()
                INSTANCE = instance
                return instance
            }
        }
        fun databaseCallback() = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                GlobalScope.launch {
                    INSTANCE?.let { prepopulateDatabase(it) }
                }
            }
        }
    }
}


suspend fun prepopulateDatabase(database: AppDatabase) {
    val initialIngredients = listOf(
        Igredient(nazwa = "Salt"),
        Igredient(nazwa = "Sugar"),
        Igredient(nazwa = "Flour"),
        Igredient(nazwa = "Rice"),
        Igredient(nazwa = "Pasta"),
        Igredient(nazwa = "Olive Oil"),
        Igredient(nazwa = "Vegetable Oil"),
        Igredient(nazwa = "Butter"),
        Igredient(nazwa = "Chicken Breast"),
        Igredient(nazwa = "Ground Beef"),
        Igredient(nazwa = "Eggs"),
        Igredient(nazwa = "Milk"),
        Igredient(nazwa = "Yogurt"),
        Igredient(nazwa = "Cheese"),
        Igredient(nazwa = "Tomatoes"),
        Igredient(nazwa = "Onions"),
        Igredient(nazwa = "Garlic"),
        Igredient(nazwa = "Bell Peppers"),
        Igredient(nazwa = "Carrots"),
        Igredient(nazwa = "Broccoli"),
        Igredient(nazwa = "Spinach"),
        Igredient(nazwa = "Lettuce"),
        Igredient(nazwa = "Cucumber"),
        Igredient(nazwa = "Potatoes"),
        Igredient(nazwa = "Sweet Potatoes"),
        Igredient(nazwa = "Lentils"),
        Igredient(nazwa = "Chickpeas"),
        Igredient(nazwa = "Black Beans"),
        Igredient(nazwa = "Corn"),
        Igredient(nazwa = "Apples"),
        Igredient(nazwa = "Bananas"),
        Igredient(nazwa = "Oranges"),
        Igredient(nazwa = "Lemons"),
        Igredient(nazwa = "Berries"),
        Igredient(nazwa = "Nuts"),
        Igredient(nazwa = "Oats"),
        Igredient(nazwa = "Honey"),
        Igredient(nazwa = "Soy Sauce"),
        Igredient(nazwa = "Vinegar"),
        Igredient(nazwa = "Black Pepper"),
        Igredient(nazwa = "Paprika")
    )

    val initialUnits = listOf(
        Unit(jednostka = "g"),
        Unit(jednostka = "ml"),
        Unit(jednostka = "sztuka"),
        Unit(jednostka = "lyzka"),
        Unit(jednostka = "lyzeczka")
    )

    // Insert each ingredient individually
    initialIngredients.forEach { ingredient ->
        database.igredientDao().insert(ingredient)
        Log.d("AppDatabase", "added")
    }

    // Insert each unit individually
    initialUnits.forEach { unit ->
        database.unitDao().insert(unit)
    }
}