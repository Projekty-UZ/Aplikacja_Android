package com.example.aplikacja_android.ui.viewModels

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aplikacja_android.database.Repository
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Recipe
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository:Repository):ViewModel() {
    val allIgredients:LiveData<List<Igredient>> = repository.igredients
    fun addRecipe(){
        viewModelScope.launch {
            repository.createRecipe(Recipe(nazwa = "xd", instrukcja = "xd", rodzaj = "xd"))
        }
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