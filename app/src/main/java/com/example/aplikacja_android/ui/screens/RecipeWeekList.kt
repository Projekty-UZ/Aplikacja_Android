package com.example.aplikacja_android.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.Date

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("NewApi")
@Composable
fun RecipeWeekList(startWeek: Long, endWeek: Long){
    val startDay = Date(startWeek).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val endDay = Date(endWeek).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val databaseViewModel = LocalDatabaseViewModel.current

    val recipesForWeek = remember { mutableStateListOf<Recipe>() }

    LaunchedEffect(Unit) {
        GlobalScope.launch {
            for (i in 0..6) {
                val mealsOnCertainDate = databaseViewModel.getAllRecipesOfDate(startDay.plusDays(i.toLong()))
                mealsOnCertainDate.forEach { meal ->
                    recipesForWeek.add(databaseViewModel.getSingleRecipe(meal.recipeId))
                }
            }
        }
    }
    LazyColumn(
        modifier = Modifier.
        fillMaxWidth().
        padding(16.dp,30.dp,16.dp,16.dp)
    ) {
        item{
            Text(text = "All recipes this week")
        }
        items(recipesForWeek.size){ index ->
            Text(text = recipesForWeek[index].nazwa)
        }
    }
}