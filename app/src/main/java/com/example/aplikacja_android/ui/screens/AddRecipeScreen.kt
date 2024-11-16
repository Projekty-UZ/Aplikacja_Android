package com.example.aplikacja_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikacja_android.database.dao.IngredientWithUnit
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef
import com.example.aplikacja_android.database.models.Unit
import com.example.aplikacja_android.navigation.Screens
import com.example.aplikacja_android.ui.viewModels.DatabaseViewModel
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(navController: NavController){
    val databaseViewModel = LocalDatabaseViewModel.current
    val availableIngredient = databaseViewModel.allIgredients.observeAsState(emptyList())
    val availableUnits = databaseViewModel.allUnits.observeAsState(emptyList())

    var recipeName by remember { mutableStateOf("") }
    var recipeType by remember { mutableStateOf("") }
    var recipeInstruction by remember { mutableStateOf("") }

    var recipeIngredientList by remember { mutableStateOf(mutableListOf(IngredientWithUnit(ingredient = Igredient(0,"",""), unit = Unit(0,""), ilosc = 0))) }

    var errorMessege by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier.
        fillMaxWidth().
        verticalScroll(scrollState).
        padding(16.dp,30.dp,16.dp,16.dp)
    ) {
            // Recipe name field
            OutlinedTextField(
                value = recipeName,
                onValueChange = { recipeName = it },
                label = { Text("Recipe Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Recipe type field
            OutlinedTextField(
                value = recipeType,
                onValueChange = { recipeType = it },
                label = { Text("Recipe Type") },
                modifier = Modifier.fillMaxWidth()
            )

            // Recipe instructions field
            OutlinedTextField(
                value = recipeInstruction,
                onValueChange = { recipeInstruction = it },
                label = { Text("Instructions") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )


            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Ingredients")

            recipeIngredientList.forEachIndexed { index, ingredient ->
                var expanded by remember { mutableStateOf(false) }
                var selectedIngredient by remember { mutableStateOf<Igredient?>(null) }

                Text("Ingredient nr" + (index + 1))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = selectedIngredient?.nazwa ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        availableIngredient.value.forEach { ingredientOption ->
                            DropdownMenuItem(
                                text = { Text(text = ingredientOption.nazwa) },
                                onClick = {
                                    selectedIngredient = ingredientOption
                                    expanded = false
                                    recipeIngredientList[index] = ingredient.copy(
                                        ingredient = ingredientOption
                                    )
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
                var textValue by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = textValue,
                    onValueChange = { newValue ->
                        if(newValue.all{it.isDigit()}) {
                            textValue = newValue
                            recipeIngredientList[index] = ingredient.copy(ilosc = textValue.toInt())
                        }
                    },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                var expanded2 by remember { mutableStateOf(false) }
                var selectedUnit by remember { mutableStateOf<Unit?>(null) }

                ExposedDropdownMenuBox(
                    expanded = expanded2,
                    onExpandedChange = { expanded2 = !expanded2 }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = selectedUnit?.jednostka ?: "Jednostka",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2) }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded2,
                        onDismissRequest = { expanded2 = false }) {
                        availableUnits.value.forEach { unitOption ->
                            DropdownMenuItem(
                                text = { Text(text = unitOption.jednostka) },
                                onClick = {
                                    selectedUnit = unitOption
                                    expanded2 = false
                                    recipeIngredientList[index] = ingredient.copy(
                                        unit = unitOption
                                    )
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Remove button
                Button(
                    onClick = { recipeIngredientList = recipeIngredientList.toMutableList().apply {
                        removeAt(index)
                    } },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Remove Ingredient")
                }
            }

        //add next ingredient button
        Button(
            onClick = {
                recipeIngredientList = recipeIngredientList.toMutableList().apply {
                    add(IngredientWithUnit(Igredient(0,"",""), Unit(0, ""), ilosc = 0))
                }
            }
        ) {
                Text("Add Ingredient")
            }
        //save recipe to db
        Button(
            modifier = Modifier.padding(0.dp,0.dp,0.dp,100.dp),
            onClick = {
                errorMessege = validateData(recipeIngredientList,recipeName,recipeType,recipeInstruction)
                if(errorMessege == null) {
                    saveRecipe(
                        databaseViewModel,
                        recipeIngredientList,
                        recipeName,
                        recipeType,
                        recipeInstruction
                    )
                    navController.navigate(Screens.RecipeListScreen.route)
                }
            }
        ) {
            Text("Save")
        }
        Box(
            modifier = Modifier.padding(0.dp,0.dp,0.dp,100.dp),
            contentAlignment = Alignment.Center
        ){
            Text(text = errorMessege ?: "", color = Color.Red)
        }
    }
}

fun saveRecipe(
    databaseViewModel: DatabaseViewModel,
    igredients:List<IngredientWithUnit>,
    name: String,
    type: String,
    instruction: String) {
    GlobalScope.launch {
        val recipeId = databaseViewModel.saveRecipe(
            Recipe(
                nazwa = name,
                rodzaj = type,
                instrukcja = instruction
            )
        )
        val newRecipe = databaseViewModel.getSingleRecipe(recipeId.toInt())
        igredients.forEach { ingredient ->
            databaseViewModel.saveCrossRef(RecipeIgredientCrossRef(
                recipeId = newRecipe.id,
                igredientId = ingredient.ingredient.id,
                unitId = ingredient.unit.unitId,
                ilosc = ingredient.ilosc
            ))
        }
    }
}

fun validateData(igredients:List<IngredientWithUnit>,name: String,type: String,instruction: String):String?{
    if (name.isBlank()) {
        return "Recipe name cannot be empty."
    }
    if (type.isBlank()) {
        return "Recipe type cannot be empty."
    }
    if (instruction.isBlank()) {
        return "Instructions cannot be empty."
    }
    if (igredients.isEmpty() || igredients.all { it.ilosc <= 0 || it.ingredient.id <= 0 || it.ingredient.nazwa == "" || it.unit.jednostka == "" || it.unit.unitId <= 0}) {
        return "Incorrect ingredients values"
    }
    return null
}
