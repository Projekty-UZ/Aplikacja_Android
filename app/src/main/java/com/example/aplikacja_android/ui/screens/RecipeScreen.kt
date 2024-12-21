package com.example.aplikacja_android.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplikacja_android.database.models.Note
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.RecipeTags
import com.example.aplikacja_android.navigation.Screens
import com.example.aplikacja_android.ui.viewModels.DatabaseViewModel
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun RecipeScreen(recipe: Recipe,navController: NavController){

    val scrollState = rememberScrollState()
    val databaseViewModel = LocalDatabaseViewModel.current
    val recipeIngredients = databaseViewModel.getIngredientsOfRecipe(recipe.id).observeAsState(emptyList())
    val nutrientValues = databaseViewModel.calculateRecipeNutrients(recipe.id).observeAsState(emptyMap())
    val recipeNotes = databaseViewModel.getNotesForRecipe(recipe.id).observeAsState(emptyList())
    val recipeTags = databaseViewModel.getTagsByRecipeId(recipe.id).observeAsState(emptyList())

    val newTagName = remember { mutableStateOf("") }

    var newNoteValue by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(recipe.isFavorite) }

    Column (
        modifier = Modifier.
        fillMaxWidth().
        verticalScroll(scrollState).
        padding(16.dp,30.dp,16.dp,16.dp)
    ){
        Text(text=recipe.nazwa)
        Text(text=recipe.instrukcja)
        Text(text = recipe.rodzaj)

        //add to favorites button
        Button(
            onClick = {
                GlobalScope.launch {
                    isFavorite = !isFavorite
                    databaseViewModel.updateRecipe(recipe.copy(isFavorite = isFavorite))
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            if(isFavorite){
                Text("Remove from favorites")
            } else {
                Text("Add to favorites")
            }
        }

        recipeIngredients.value.forEach{ ingredient ->
            Text(text= ingredient.ingredient.nazwa+","+ingredient.ilosc+","+ingredient.unit.jednostka)
        }

        nutrientValues.value.let { nutrients ->
            Text(text = "Total Nutritional Values:")
            Text(text = "Kalorie: ${"%.2f".format(nutrients["kalorie"] ?: 0.0)} kcal")
            Text(text = "Białko: ${"%.2f".format(nutrients["bialko"] ?: 0.0)} g")
            Text(text = "Tłuszcz: ${"%.2f".format(nutrients["tluszcz"] ?: 0.0)} g")
            Text(text = "Węglowodany: ${"%.2f".format(nutrients["weglowodany"] ?: 0.0)} g")
        }

        RecipeNotes(recipeNotes.value,databaseViewModel)

        OutlinedTextField(
            value = newNoteValue,
            onValueChange = { newNoteValue = it },
            label = { Text("Add a new note") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Button(
            onClick = {
                if (newNoteValue.isNotBlank()) {
                    GlobalScope.launch {
                        databaseViewModel.insertNoteForRecipe(
                            Note(
                                recipeId = recipe.id,
                                noteValue = newNoteValue
                            )
                        )
                        newNoteValue = ""
                    }
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Add Note")
        }
        Text("Tags")
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = newTagName.value,
                onValueChange = { newTagName.value = it },
                label = { Text("New Tag") }
            )
            Button(
                onClick = {
                    GlobalScope.launch {
                        databaseViewModel.insertTag(RecipeTags(name= newTagName.value, recipeId = recipe.id))
                        newTagName.value = ""
                    }
                }
            ) {
                Text("Add Tag")
            }
        }

        recipeTags.value.forEach { tag ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(tag.name)
                Button(
                    onClick = {
                        GlobalScope.launch {
                            databaseViewModel.deleteTag(tag)
                        }
                    }
                ) {
                    Text("Remove Tag")
                }
            }
        }
    }
    FloatingActionButton(
        modifier = Modifier.padding(300.dp,650.dp,0.dp,0.dp),
        onClick = {
            navController.navigate(Screens.EditRecipeScreen.createRoute(recipe.id))
                  },
    ) {
        Text(text = "Edit Recipe")
    }
}

@Composable
fun RecipeNotes(notes: List<Note>,databaseViewModel: DatabaseViewModel){
    Text(text = "Recipe notes:", fontSize = 30.sp)
    notes.forEachIndexed{ index,note ->
        Text(text = "Note ${index+1}")
        Row(modifier = Modifier.fillMaxWidth())
        {
            Text(text = note.noteValue, modifier = Modifier.padding(0.dp,10.dp,0.dp,0.dp))
            IconButton(
                onClick = {
                    GlobalScope.launch {
                        databaseViewModel.deleteNoteForRecipe(note)
                    }
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Usuń note")
            }
        }
    }
}