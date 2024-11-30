package com.example.aplikacja_android.ui.composables

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.aplikacja_android.database.models.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeSelection(
    aboveText: String,
    isExpanded: Boolean,
    meal: Recipe?,
    recipes: List<Recipe>?,
    onExpandedChange:(Boolean) -> Unit,
    onMealChange: (Recipe?) -> Unit
)
{
    Text(aboveText)
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { onExpandedChange(!isExpanded)}
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = meal?.nazwa ?: "Select Recipe",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandedChange(false) }) {
            DropdownMenuItem(
                text = {Text(text = "Bez Posilku")},
                onClick = {
                    onMealChange(null)
                    onExpandedChange(false)
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
            )
            recipes?.forEach { recipe ->
                DropdownMenuItem(
                    text = { Text(text = recipe.nazwa) },
                    onClick = {
                        onMealChange(recipe)
                        onExpandedChange(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}