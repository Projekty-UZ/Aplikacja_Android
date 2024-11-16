package com.example.aplikacja_android.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.ShoppingItem
import com.example.aplikacja_android.database.models.ShoppingList
import com.example.aplikacja_android.navigation.Screens
import com.example.aplikacja_android.ui.viewModels.DatabaseViewModel
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.component1
import kotlin.collections.component2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShoppingListScreen(navController: NavController, shoppingList: ShoppingList){
    val databaseViewModel = LocalDatabaseViewModel.current
    val items by databaseViewModel.getItemsForShoppingList(shoppingList.id).observeAsState(emptyList())
    val allIngredients = databaseViewModel.allIgredients.observeAsState(emptyList())

    val scrollState = rememberScrollState()
    val categorizedItems = items.groupBy { it.kategoria }

    var chosenIngredient by remember { mutableStateOf<Igredient?>(null) }
    var expanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            "Szczegóły listy: ${shoppingList.name}",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        categorizedItems.forEach { (kategoria, itemsInCategory) ->
            Text(
                text = kategoria,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            itemsInCategory.forEach { item ->
                ShoppingListItemRow(item = item, viewModel = databaseViewModel)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    GlobalScope.launch {
                        val newItem = ShoppingItem(
                            listId = shoppingList.id,
                            name = chosenIngredient?.nazwa ?: "",
                            quantity = 1,
                            kategoria = chosenIngredient?.kategoria ?: ""
                        )
                        databaseViewModel.createShoppingItem(newItem)
                    }
                },
                enabled = !chosenIngredient?.nazwa.equals(null)
            ) {
                Text("Dodaj produkt")
            }


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.padding(20.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    value = chosenIngredient?.nazwa ?: "Select Ingredient",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    allIngredients.value.forEach { ingredientOption ->
                        DropdownMenuItem(
                            text = { Text(text = ingredientOption.nazwa) },
                            onClick = {
                                chosenIngredient = ingredientOption
                                expanded = false
                                Log.d("new ingredient",ingredientOption.nazwa)
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                GlobalScope.launch{
                    databaseViewModel.updateShoppingList(shoppingList.copy(isTemplate = true))
                }
                navController.navigate(Screens.CreateShoppingListFromTemplateScreen.createRoute(shoppingList.id))
            }
        ) {
            Text("Użyj jako szablon")
        }
    }
}

@Composable
fun ShoppingListItemRow(item: ShoppingItem, viewModel: DatabaseViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var quantity by remember { mutableStateOf(item.quantity) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        // Pole do edycji ilości składnika
        OutlinedTextField(
            value = quantity.toString(),
            onValueChange = { newQuantity ->
                // If the input is blank, set the quantity to 0
                val sanitizedQuantity = if (newQuantity.isBlank()) {
                    0
                } else if (newQuantity.all { it.isDigit() }) {
                    newQuantity.toInt() // Only update if the input is a valid number
                } else {
                    quantity // Keep the old value if it's not a valid number
                }

                // Update the quantity only if it has changed
                if (sanitizedQuantity != quantity) {
                    quantity = sanitizedQuantity
                    coroutineScope.launch {
                        viewModel.updateShoppingItemQuantity(item.itemId, quantity)
                    }
                }
            },
            modifier = Modifier.width(80.dp),
            label = { Text("Ilość") }
        )

        Checkbox(
            checked = item.isBought,
            onCheckedChange = { isChecked ->
                coroutineScope.launch {
                    viewModel.updateItemBoughtStatus(item.itemId, isChecked)
                }
            }
        )

        // Przycisk do usunięcia składnika z listy
        IconButton(
            onClick = {
                coroutineScope.launch {
                    viewModel.deleteShoppingItem(item)
                }
            }
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Usuń produkt")
        }
    }
}