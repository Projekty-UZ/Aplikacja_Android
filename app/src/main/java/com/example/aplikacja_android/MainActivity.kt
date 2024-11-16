package com.example.aplikacja_android

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.aplikacja_android.navigation.BottomNavBar
import com.example.aplikacja_android.navigation.BottomNavItem
import com.example.aplikacja_android.navigation.Navigation
import com.example.aplikacja_android.navigation.Screens
import com.example.aplikacja_android.ui.theme.Aplikacja_AndroidTheme
import com.example.aplikacja_android.ui.viewModels.DatabaseViewModel
import com.example.aplikacja_android.ui.viewModels.DatabaseViewModelFactory
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    val databaseViewModel: DatabaseViewModel by viewModels<DatabaseViewModel>{
        DatabaseViewModelFactory((application as AndroidApp).repository)
    }


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Aplikacja_AndroidTheme {
                CompositionLocalProvider(
                    LocalDatabaseViewModel provides databaseViewModel
                ) {
                    var showMessage by remember { mutableStateOf(true) }

                    val context = LocalContext.current

                    @SuppressLint("NewApi")
                    val currentHour = LocalTime.now().hour
                    val welcomeMessage = when {
                        currentHour == 10 -> "Czas na sniadanie"
                        currentHour == 15 -> "Czas na obiad"
                        currentHour == 19 -> "Czas na kolacje"
                        else -> "Witaj"
                    }

                    val navController = rememberNavController()
                    val currentBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = currentBackStackEntry?.destination?.route

                    LaunchedEffect(Unit) {
                        Toast.makeText(context, welcomeMessage, Toast.LENGTH_LONG).show()
                    }

                    Scaffold(
                        bottomBar = {
                            BottomNavBar(
                                items = listOf(
                                    BottomNavItem(
                                        Screens.RecipeListScreen.route,
                                        "Recipes",
                                        painterResource(id = R.drawable.recipes)
                                    ),
                                    BottomNavItem(
                                        Screens.CalendarScreen.route,
                                        "Calendar",
                                        painterResource(id = R.drawable.calendar)
                                    ),
                                    BottomNavItem(
                                        Screens.ShoppingListScreen.route,
                                        "Lista",
                                        painterResource(id = R.drawable.lista)
                                    ),
                                    BottomNavItem(
                                        Screens.IngredientList.route,
                                        "Składniki",
                                        painterResource(id = R.drawable.ingredient)
                                    )
                                ),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                    ) {
                        Navigation(navcontroller = navController)
                    }
                }
            }
        }
    }
}
