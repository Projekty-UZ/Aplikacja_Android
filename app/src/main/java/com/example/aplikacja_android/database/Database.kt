package com.example.aplikacja_android.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aplikacja_android.database.converters.DateConverter
import com.example.aplikacja_android.database.dao.CalendarMealDao
import com.example.aplikacja_android.database.dao.IgredientDao
import com.example.aplikacja_android.database.dao.RecipeDao
import com.example.aplikacja_android.database.dao.RecipeIgredientCrossRefDao
import com.example.aplikacja_android.database.dao.ShoppingItemDao
import com.example.aplikacja_android.database.dao.ShoppingListDao
import com.example.aplikacja_android.database.dao.TipDao
import com.example.aplikacja_android.database.dao.UnitDao
import com.example.aplikacja_android.database.models.CalendarMeal
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef
import com.example.aplikacja_android.database.models.ShoppingItem
import com.example.aplikacja_android.database.models.ShoppingList
import com.example.aplikacja_android.database.models.Tip
import com.example.aplikacja_android.database.models.Unit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [
    Recipe::class,
    Igredient::class,
    RecipeIgredientCrossRef::class,
    Unit::class,
    CalendarMeal::class,
    ShoppingList::class,
    ShoppingItem::class,
    Tip::class
                     ], version = 4, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract  class AppDatabase: RoomDatabase(){
    abstract fun recipeDao(): RecipeDao
    abstract fun igredientDao(): IgredientDao
    abstract fun recipeIgredientCrossRefDao(): RecipeIgredientCrossRefDao
    abstract fun unitDao(): UnitDao
    abstract fun calendarMealDao(): CalendarMealDao
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingItemDao(): ShoppingItemDao
    abstract fun tipDao(): TipDao
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
        Igredient(nazwa = "Salt", kategoria = "Przyprawy"),
        Igredient(nazwa = "Sugar", kategoria = "Przyprawy"),
        Igredient(nazwa = "Flour", kategoria = "Zboża"),
        Igredient(nazwa = "Rice", kategoria = "Zboża"),
        Igredient(nazwa = "Pasta", kategoria = "Zboża"),
        Igredient(nazwa = "Olive Oil", kategoria = "Tłuszcze"),
        Igredient(nazwa = "Vegetable Oil", kategoria = "Tłuszcze"),
        Igredient(nazwa = "Butter", kategoria = "Nabiał"),
        Igredient(nazwa = "Chicken Breast", kategoria = "Mięso"),
        Igredient(nazwa = "Ground Beef", kategoria = "Mięso"),
        Igredient(nazwa = "Eggs", kategoria = "Nabiał"),
        Igredient(nazwa = "Milk", kategoria = "Nabiał"),
        Igredient(nazwa = "Yogurt", kategoria = "Nabiał"),
        Igredient(nazwa = "Cheese", kategoria = "Nabiał"),
        Igredient(nazwa = "Tomatoes", kategoria = "Warzywa"),
        Igredient(nazwa = "Onions", kategoria = "Warzywa"),
        Igredient(nazwa = "Garlic", kategoria = "Warzywa"),
        Igredient(nazwa = "Bell Peppers", kategoria = "Warzywa"),
        Igredient(nazwa = "Carrots", kategoria = "Warzywa"),
        Igredient(nazwa = "Broccoli", kategoria = "Warzywa"),
        Igredient(nazwa = "Spinach", kategoria = "Warzywa"),
        Igredient(nazwa = "Lettuce", kategoria = "Warzywa"),
        Igredient(nazwa = "Cucumber", kategoria = "Warzywa"),
        Igredient(nazwa = "Potatoes", kategoria = "Warzywa"),
        Igredient(nazwa = "Sweet Potatoes", kategoria = "Warzywa"),
        Igredient(nazwa = "Lentils", kategoria = "Rośliny strączkowe"),
        Igredient(nazwa = "Chickpeas", kategoria = "Rośliny strączkowe"),
        Igredient(nazwa = "Black Beans", kategoria = "Rośliny strączkowe"),
        Igredient(nazwa = "Corn", kategoria = "Warzywa"),
        Igredient(nazwa = "Apples", kategoria = "Owoce"),
        Igredient(nazwa = "Bananas", kategoria = "Owoce"),
        Igredient(nazwa = "Oranges", kategoria = "Owoce"),
        Igredient(nazwa = "Lemons", kategoria = "Owoce"),
        Igredient(nazwa = "Berries", kategoria = "Owoce"),
        Igredient(nazwa = "Nuts", kategoria = "Przekąski"),
        Igredient(nazwa = "Oats", kategoria = "Zboża"),
        Igredient(nazwa = "Honey", kategoria = "Przyprawy"),
        Igredient(nazwa = "Soy Sauce", kategoria = "Przyprawy"),
        Igredient(nazwa = "Vinegar", kategoria = "Przyprawy"),
        Igredient(nazwa = "Black Pepper", kategoria = "Przyprawy"),
        Igredient(nazwa = "Paprika", kategoria = "Przyprawy")
    )

    val initialUnits = listOf(
        Unit(jednostka = "g"),
        Unit(jednostka = "ml"),
        Unit(jednostka = "sztuka"),
        Unit(jednostka = "lyzka"),
        Unit(jednostka = "lyzeczka")
    )

    val initialTips = listOf(
        Tip(ingredient = "bread", tip = "Store bread in a paper bag at room temperature to maintain its crusty texture and avoid mold."),
        Tip(ingredient = "cheese", tip = "Wrap cheese in wax paper and store in the refrigerator to allow it to breathe and prevent drying out."),
        Tip(ingredient = "carrots", tip = "Keep carrots in a plastic bag in the crisper drawer of your refrigerator to retain moisture."),
        Tip(ingredient = "milk", tip = "Store milk in the coldest part of the refrigerator, not in the door, to keep it fresh longer."),
        Tip(ingredient = "apples", tip = "Refrigerate apples in a crisper drawer or in a plastic bag with holes to maintain freshness."),
        Tip(ingredient = "herbs", tip = "Wrap fresh herbs in a damp paper towel and store them in an airtight container in the refrigerator."),
        Tip(ingredient = "eggs", tip = "Keep eggs in their original carton in the refrigerator to protect them from odors and maintain freshness."),
        Tip(ingredient = "onions", tip = "Store onions in a cool, dry, and well-ventilated place, away from potatoes to prevent sprouting."),
        Tip(ingredient = "olive oil", tip = "Keep olive oil in a dark, cool cupboard to protect it from light and heat, which can cause it to spoil."),
        Tip(ingredient = "bananas", tip = "Store bananas at room temperature, away from other fruits, to slow down ripening.")
    )

    // Insert each ingredient individually
    initialIngredients.forEach { ingredient ->
        database.igredientDao().insert(ingredient)
    }

    // Insert each unit individually
    initialUnits.forEach { unit ->
        database.unitDao().insert(unit)
    }

    // Insert each tip individually
    initialTips.forEach { tip ->
        database.tipDao().insertTip(tip)
    }
}