package com.example.aplikacja_android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aplikacja_android.database.converters.DateConverter
import com.example.aplikacja_android.database.dao.ActivityDao
import com.example.aplikacja_android.database.dao.ActivityTypeDao
import com.example.aplikacja_android.database.dao.BloodPressureMeasurmentsDao
import com.example.aplikacja_android.database.dao.BloodSugarMeasurmentDao
import com.example.aplikacja_android.database.dao.BodyMeasurementsDao
import com.example.aplikacja_android.database.dao.CalendarMealDao
import com.example.aplikacja_android.database.dao.DailyWeightDao
import com.example.aplikacja_android.database.dao.IgredientDao
import com.example.aplikacja_android.database.dao.MacrosDao
import com.example.aplikacja_android.database.dao.NoteDao
import com.example.aplikacja_android.database.dao.RecipeDao
import com.example.aplikacja_android.database.dao.RecipeIgredientCrossRefDao
import com.example.aplikacja_android.database.dao.ShoppingItemDao
import com.example.aplikacja_android.database.dao.ShoppingListDao
import com.example.aplikacja_android.database.dao.TipDao
import com.example.aplikacja_android.database.dao.UnitDao
import com.example.aplikacja_android.database.models.CalendarMeal
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Macros
import com.example.aplikacja_android.database.models.Note
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.Unit
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef
import com.example.aplikacja_android.database.models.ShoppingItem
import com.example.aplikacja_android.database.models.ShoppingList
import com.example.aplikacja_android.database.models.Tip
import com.example.aplikacja_android.database.models.Activity
import com.example.aplikacja_android.database.models.ActivityType
import com.example.aplikacja_android.database.models.BloodPressureMeasurement
import com.example.aplikacja_android.database.models.BloodSugarMeasurement
import com.example.aplikacja_android.database.models.BodyMeasurements
import com.example.aplikacja_android.database.models.DailyWeight
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
    Tip::class,
    Note::class,
    Macros::class,
    Activity::class,
    ActivityType::class,
    BodyMeasurements::class,
    BloodPressureMeasurement::class,
    BloodSugarMeasurement::class,
    DailyWeight::class
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
    abstract fun noteDao(): NoteDao
    abstract fun macrosDao(): MacrosDao
    abstract fun activityDao(): ActivityDao
    abstract fun activityTypeDao(): ActivityTypeDao
    abstract fun bodyMeasurementsDao(): BodyMeasurementsDao
    abstract fun bloodPressureMeasurementDao(): BloodPressureMeasurmentsDao
    abstract fun bloodSugarMeasurementDao(): BloodSugarMeasurmentDao
    abstract fun dailyWeightDao(): DailyWeightDao
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
        Igredient(nazwa="Salt", kategoria="Przyprawy", kalorie=0.0, bialko=0.0, tluszcz=0.0, weglowodany=0.0),
        Igredient(nazwa="Sugar", kategoria="Przyprawy", kalorie=387.0, bialko=0.0, tluszcz=0.0, weglowodany=100.0),
        Igredient(nazwa="Flour", kategoria="Zboza", kalorie=364.0, bialko=10.0, tluszcz=1.0, weglowodany=76.0),
        Igredient(nazwa="Rice", kategoria="Zboza", kalorie=365.0, bialko=7.0, tluszcz=1.0, weglowodany=80.0),
        Igredient(nazwa="Pasta", kategoria="Zboza", kalorie=371.0, bialko=13.0, tluszcz=1.5, weglowodany=74.0),
        Igredient(nazwa="Olive Oil", kategoria="Tluszcze", kalorie=884.0, bialko=0.0, tluszcz=100.0, weglowodany=0.0),
        Igredient(nazwa="Vegetable Oil", kategoria="Tluszcze", kalorie=884.0, bialko=0.0, tluszcz=100.0, weglowodany=0.0),
        Igredient(nazwa="Butter", kategoria="Nabial", kalorie=717.0, bialko=0.9, tluszcz=81.0, weglowodany=0.1),
        Igredient(nazwa="Chicken Breast", kategoria="Mieso", kalorie=165.0, bialko=31.0, tluszcz=3.6, weglowodany=0.0),
        Igredient(nazwa="Ground Beef", kategoria="Mieso", kalorie=250.0, bialko=26.0, tluszcz=17.0, weglowodany=0.0),
        Igredient(nazwa="Eggs", kategoria="Nabial", kalorie=143.0, bialko=13.0, tluszcz=10.0, weglowodany=1.0),
        Igredient(nazwa="Milk", kategoria="Nabial", kalorie=42.0, bialko=3.4, tluszcz=1.0, weglowodany=5.0),
        Igredient(nazwa="Yogurt", kategoria="Nabial", kalorie=59.0, bialko=10.0, tluszcz=0.4, weglowodany=4.0),
        Igredient(nazwa="Cheese", kategoria="Nabial", kalorie=402.0, bialko=25.0, tluszcz=33.0, weglowodany=1.3),
        Igredient(nazwa="Tomatoes", kategoria="Warzywa", kalorie=18.0, bialko=0.9, tluszcz=0.2, weglowodany=4.0),
        Igredient(nazwa="Onions", kategoria="Warzywa", kalorie=40.0, bialko=1.1, tluszcz=0.1, weglowodany=9.0),
        Igredient(nazwa="Garlic", kategoria="Warzywa", kalorie=149.0, bialko=6.4, tluszcz=0.5, weglowodany=33.0),
        Igredient(nazwa="Bell Peppers", kategoria="Warzywa", kalorie=31.0, bialko=1.0, tluszcz=0.3, weglowodany=6.0),
        Igredient(nazwa="Carrots", kategoria="Warzywa", kalorie=41.0, bialko=0.9, tluszcz=0.2, weglowodany=10.0),
        Igredient(nazwa="Broccoli", kategoria="Warzywa", kalorie=34.0, bialko=2.8, tluszcz=0.4, weglowodany=7.0),
        Igredient(nazwa="Spinach", kategoria="Warzywa", kalorie=23.0, bialko=2.9, tluszcz=0.4, weglowodany=3.6),
        Igredient(nazwa="Lettuce", kategoria="Warzywa", kalorie=15.0, bialko=1.4, tluszcz=0.2, weglowodany=2.9),
        Igredient(nazwa="Cucumber", kategoria="Warzywa", kalorie=16.0, bialko=0.7, tluszcz=0.1, weglowodany=3.6),
        Igredient(nazwa="Potatoes", kategoria="Warzywa", kalorie=77.0, bialko=2.0, tluszcz=0.1, weglowodany=17.0),
        Igredient(nazwa="Sweet Potatoes", kategoria="Warzywa", kalorie=86.0, bialko=1.6, tluszcz=0.1, weglowodany=20.0),
        Igredient(nazwa="Lentils", kategoria="Rosliny straczkowe", kalorie=116.0, bialko=9.0, tluszcz=0.4, weglowodany=20.0),
        Igredient(nazwa="Chickpeas", kategoria="Rosliny straczkowe", kalorie=164.0, bialko=8.9, tluszcz=2.6, weglowodany=27.0),
        Igredient(nazwa="Black Beans", kategoria="Rosliny straczkowe", kalorie=132.0, bialko=8.9, tluszcz=0.5, weglowodany=24.0),
        Igredient(nazwa="Corn", kategoria="Warzywa", kalorie=86.0, bialko=3.3, tluszcz=1.2, weglowodany=19.0),
        Igredient(nazwa="Apples", kategoria="Owoce", kalorie=52.0, bialko=0.3, tluszcz=0.2, weglowodany=14.0),
        Igredient(nazwa="Bananas", kategoria="Owoce", kalorie=89.0, bialko=1.1, tluszcz=0.3, weglowodany=23.0),
        Igredient(nazwa="Oranges", kategoria="Owoce", kalorie=47.0, bialko=0.9, tluszcz=0.1, weglowodany=12.0),
        Igredient(nazwa="Lemons", kategoria="Owoce", kalorie=29.0, bialko=1.1, tluszcz=0.3, weglowodany=9.0),
        Igredient(nazwa="Berries", kategoria="Owoce", kalorie=57.0, bialko=1.0, tluszcz=0.3, weglowodany=14.0),
        Igredient(nazwa="Nuts", kategoria="Przekaski", kalorie=607.0, bialko=20.0, tluszcz=54.0, weglowodany=20.0),
        Igredient(nazwa="Oats", kategoria="Zboza", kalorie=389.0, bialko=17.0, tluszcz=7.0, weglowodany=66.0),
        Igredient(nazwa="Honey", kategoria="Przyprawy", kalorie=304.0, bialko=0.3, tluszcz=0.0, weglowodany=82.0),
        Igredient(nazwa="Soy Sauce", kategoria="Przyprawy", kalorie=53.0, bialko=4.9, tluszcz=0.1, weglowodany=4.9),
        Igredient(nazwa="Vinegar", kategoria="Przyprawy", kalorie=18.0, bialko=0.0, tluszcz=0.0, weglowodany=0.4),
        Igredient(nazwa="Black Pepper", kategoria="Przyprawy", kalorie=255.0, bialko=10.9, tluszcz=3.3, weglowodany=64.0),
        Igredient(nazwa="Paprika", kategoria="Przyprawy", kalorie=282.0, bialko=14.1, tluszcz=12.9, weglowodany=34.0)
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

    val initialActivityTypes = listOf(
        ActivityType(name = "Running", caloriesPerMinute = 11.4),
        ActivityType(name = "Cycling", caloriesPerMinute = 8.6),
        ActivityType(name = "Swimming", caloriesPerMinute = 9.8),
        ActivityType(name = "Walking", caloriesPerMinute = 5.0),
        ActivityType(name = "Yoga", caloriesPerMinute = 2.5),
        ActivityType(name = "Weightlifting", caloriesPerMinute = 3.0),
        ActivityType(name = "Dancing", caloriesPerMinute = 6.0),
        ActivityType(name = "Hiking", caloriesPerMinute = 6.0),
        ActivityType(name = "Pilates", caloriesPerMinute = 3.0),
        ActivityType(name = "Rowing", caloriesPerMinute = 7.0)
    )

    // Insert each ingredient individually
    initialIngredients.forEach { ingredient ->
        database.igredientDao().insert(ingredient)
    }

    // Insert each unit individually
    initialUnits.forEach { units ->
        database.unitDao().insert(units)
    }

    // Insert each tip individually
    initialTips.forEach { tip ->
        database.tipDao().insertTip(tip)
    }

    // Insert each activity type individually
    initialActivityTypes.forEach { activityType ->
        database.activityTypeDao().insert(activityType)
    }

    database.macrosDao().insertOrUpdate(Macros(0, 2000.0, 50.0, 70.0, 300.0))
    database.bodyMeasurementsDao().insert(BodyMeasurements(0, 180.0, 80.0, 100.0, 90.0, 50.0, 30.0, 40.0))
}