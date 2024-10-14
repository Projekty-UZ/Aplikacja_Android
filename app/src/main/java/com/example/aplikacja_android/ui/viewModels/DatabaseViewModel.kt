package com.example.aplikacja_android.ui.viewModels

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aplikacja_android.database.Repository
import com.example.aplikacja_android.database.dao.IngredientWithUnit
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef
import com.example.aplikacja_android.database.models.Unit
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository:Repository):ViewModel() {
    val allIgredients:LiveData<List<Igredient>> = repository.igredients
    val allRecipes: LiveData<List<Recipe>> = repository.recipes
    val allUnits: LiveData<List<Unit>> = repository.units

    val selectedRecipe = MutableLiveData<Recipe?>()

    fun selectRecipe(recipe: Recipe){
        selectedRecipe.value = recipe
    }

    suspend fun saveRecipe(recipe: Recipe):Long {
        return repository.createRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe){
        repository.updateRecipe(recipe)
    }

    suspend fun deleteRecipesWithCrossRef(recipe: Recipe){
        repository.deleteRecipe(recipe)
    }

    fun getSingleRecipe(id: Int): Recipe {
        return repository.getSingleRecipe(id)
    }

    fun getIngredientsOfRecipe(id: Int): LiveData<List<IngredientWithUnit>> {
        return repository.getIgredientsOfRecipe(id)
    }

    fun getRecipesWithIngredient(id: Int): LiveData<List<Recipe>>{
        return repository.getRecipesWithIgredient(id)
    }


    suspend fun deleteCrossRef(recipeIgredientCrossRef: RecipeIgredientCrossRef){
        repository.deleteRecipeIgredientCrossRef(recipeIgredientCrossRef)
    }

    suspend fun saveCrossRef(recipeIgredientCrossRef: RecipeIgredientCrossRef){
        repository.createRecipeIgredientCrossRef(recipeIgredientCrossRef)
    }

}
class DatabaseViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DatabaseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

val LocalDatabaseViewModel = staticCompositionLocalOf<DatabaseViewModel> {
    error("No DatabaseViewModel provided")
}