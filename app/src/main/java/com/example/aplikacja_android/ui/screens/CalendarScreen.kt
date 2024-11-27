package com.example.aplikacja_android.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikacja_android.database.models.CalendarMeal
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.navigation.Screens
import com.example.aplikacja_android.ui.composables.RecipeSelection
import com.example.aplikacja_android.ui.viewModels.DatabaseViewModel
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@SuppressLint("NewApi")
@Composable
fun CalendarScreen(navController: NavController) {
    val databaseViewModel = LocalDatabaseViewModel.current
    val recipes = databaseViewModel.allRecipes.observeAsState()

    val coroutineScope = rememberCoroutineScope()
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }
    var currentMonth by remember { mutableStateOf(LocalDate.now().monthValue) }

    var mealsForSelectedDate by remember { mutableStateOf(emptyList<CalendarMeal>()) }

    var selectedDay: Int? by remember { mutableStateOf(0) }
    var selectedDateText by remember { mutableStateOf("") }

    val daysOfMonth = getDaysInMonth(currentYear, currentMonth)
    val monthName = getMonthName(currentYear, currentMonth)

    var breakfastMeal by remember { mutableStateOf<Recipe?>(null) }
    var dinnerMeal by remember { mutableStateOf<Recipe?>(null) }
    var supperMeal by remember { mutableStateOf<Recipe?>(null) }

    var showingNutrients by remember { mutableStateOf(false) }
    var nutrientsValues: Map<String, Double>? by remember { mutableStateOf(emptyMap()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    selectedDay = 0
                    // Navigate to the previous month
                    if (currentMonth == 1) {
                        currentMonth = 12
                        currentYear -= 1
                    } else {
                        currentMonth -= 1
                    }
                }) {
                    Text("Previous")
                }

                // Display current month and year
                Text(
                    text = "$monthName $currentYear",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Button(onClick = {
                    selectedDay = 0
                    // Navigate to the next month
                    if (currentMonth == 12) {
                        currentMonth = 1
                        currentYear += 1
                    } else {
                        currentMonth += 1
                    }
                }) {
                    Text("Next")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Display day names
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { dayName ->
                    Text(
                        text = dayName,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display the days in a grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(daysOfMonth.size) { index ->
                    val day = daysOfMonth[index]
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .background(
                                color = if (day == selectedDay) Color.White else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                if (day != null) {
                                    selectedDay = day
                                    selectedDateText = "Selected date: $day"
                                    GlobalScope.launch {
                                        val selectedDate =
                                            LocalDate.of(currentYear, currentMonth, selectedDay!!)

                                        mealsForSelectedDate =
                                            databaseViewModel.getAllRecipesOfDate(selectedDate)

                                        breakfastMeal =
                                            mealsForSelectedDate.find { it.mealType == "Breakfast" }
                                                ?.let { databaseViewModel.getSingleRecipe(it.recipeId) }

                                        dinnerMeal =
                                            mealsForSelectedDate.find { it.mealType == "Dinner" }
                                                ?.let { databaseViewModel.getSingleRecipe(it.recipeId) }

                                        supperMeal =
                                            mealsForSelectedDate.find { it.mealType == "Supper" }
                                                ?.let { databaseViewModel.getSingleRecipe(it.recipeId) }
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(
                            text = day?.toString() ?: "",
                            style = TextStyle(
                                color = if (day == selectedDay) Color.Black else Color.White
                            )
                        )
                    }
                }
            }
            var isBreakfastExpanded by remember { mutableStateOf(false) }
            var isDinnerExpanded by remember { mutableStateOf(false) }
            var isSupperExpanded by remember { mutableStateOf(false) }

            if (selectedDay != 0) {
                RecipeSelection(
                    "Breakfast",
                    isBreakfastExpanded,
                    breakfastMeal,
                    recipes.value,
                    onExpandedChange = { isBreakfastExpanded = it },
                    onMealChange = { breakfastMeal = it }
                )
                RecipeSelection(
                    "Dinner",
                    isDinnerExpanded,
                    dinnerMeal,
                    recipes.value,
                    onExpandedChange = { isDinnerExpanded = it },
                    onMealChange = { dinnerMeal = it }
                )
                RecipeSelection(
                    "Supper",
                    isSupperExpanded,
                    supperMeal,
                    recipes.value,
                    onExpandedChange = { isSupperExpanded = it },
                    onMealChange = { supperMeal = it }
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            saveMeals(
                                selectedDay!!,
                                currentMonth,
                                currentYear,
                                breakfastMeal,
                                dinnerMeal,
                                supperMeal,
                                databaseViewModel
                            )
                            selectedDay = 0
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Save Meals")
                    }

                    Button(
                        onClick = {
                            val selectedDate =
                                LocalDate.of(currentYear, currentMonth, selectedDay!!)
                            val startOfWeek: Long? =
                                selectedDate.with(DayOfWeek.MONDAY)
                                    .atStartOfDay(ZoneId.systemDefault())
                                    ?.toInstant()?.toEpochMilli()
                            val endOfWeek: Long? =
                                selectedDate.with(DayOfWeek.SUNDAY)
                                    .atStartOfDay(ZoneId.systemDefault())
                                    ?.toInstant()?.toEpochMilli()
                            selectedDay = 0
                            navController.navigate(
                                Screens.RecipeWeekList.createRoute(
                                    startOfWeek!!,
                                    endOfWeek!!
                                )
                            )
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Show this week recipes")
                    }
                }
                Button(
                    onClick = {
                        navController.navigate(Screens.DayCalorieScreen.createRoute(selectedDay!!,currentMonth,currentYear))
                    }
                ) {
                    Text("Show calories")
                }
            }


    }
}

@SuppressLint("NewApi")
@OptIn(DelicateCoroutinesApi::class)
fun saveMeals(
    day:Int,
    month:Int,
    year: Int,
    breakfastMeal: Recipe?,
    dinnerMeal: Recipe?,
    supperMeal: Recipe?,
    databaseViewModel: DatabaseViewModel
){
    val selectedDate = LocalDate.of(year,month,day)
    GlobalScope.launch{
        databaseViewModel.deleteAllRecipesOfDate(selectedDate)
        if(breakfastMeal != null) {
            databaseViewModel.insertCalendarMeal(CalendarMeal(0, selectedDate, breakfastMeal.id,"Breakfast"))
        }
        if(dinnerMeal != null) {
            databaseViewModel.insertCalendarMeal(CalendarMeal(0, selectedDate, dinnerMeal.id,"Dinner"))
        }
        if(supperMeal != null) {
            databaseViewModel.insertCalendarMeal(CalendarMeal(0, selectedDate, supperMeal.id,"Supper"))
        }
    }
}

@SuppressLint("NewApi")
fun getDaysInMonth(year: Int, month: Int): List<Int?> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Adjust to make Sunday=0

    // Create a list to hold days, with leading nulls for the offset
    val days = MutableList<Int?>(startDayOfWeek) { null }
    days.addAll((1..daysInMonth).toList())

    return days
}

@SuppressLint("NewApi")
fun getMonthName(year: Int, month: Int): String {
    return YearMonth.of(year, month).month.name.lowercase().replaceFirstChar { it.uppercase() }
}