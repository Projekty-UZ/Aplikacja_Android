package com.example.aplikacja_android.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import com.example.aplikacja_android.R
import com.example.aplikacja_android.navigation.Screens
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlin.let

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(navController: NavController) {
    val databaseViewModel = LocalDatabaseViewModel.current
    val shoppingLists = databaseViewModel.allShoppingList.observeAsState(emptyList())

    var templateOnly by remember { mutableStateOf(false) }

    val displayedRecipes = if (templateOnly) {
        shoppingLists.value.filter { it.isTemplate == true }
    }else{
        shoppingLists.value
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text="Listy zakupów"
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.
            fillMaxSize().
            padding(0.dp,20.dp,0.dp,0.dp),
        ) {
            items(displayedRecipes) { shoppingList ->
                Log.d("ShoppingListScreen", "List Item: ${shoppingList.name}")
                Text(
                    text = shoppingList.name,
                    modifier = Modifier
                        .clickable {
                            databaseViewModel.selectShoppingList(shoppingList)
                            navController.navigate(Screens.EditShoppingListScreen.createRoute(shoppingList.id))
                        }
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
    FloatingActionButton(
        modifier = Modifier.padding(300.dp,650.dp,0.dp,0.dp),
        onClick = { navController.navigate(Screens.CreateShoppingListScreen.route) },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.add),
            contentDescription = "AddButton",
            modifier = Modifier.size(50.dp)
        )
    }
    FloatingActionButton(
        modifier = Modifier.padding(50.dp,650.dp,0.dp,0.dp),
        onClick = { templateOnly = !templateOnly },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.sort),
            contentDescription = "Show template only",
            modifier = Modifier.size(50.dp)
        )
    }
}



