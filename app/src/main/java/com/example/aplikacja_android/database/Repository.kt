package com.example.aplikacja_android.database

import com.example.aplikacja_android.database.dao.IgredientDao
import com.example.aplikacja_android.database.dao.RecipeDao
import com.example.aplikacja_android.database.dao.RecipeIgredientCrossRefDao
import com.example.aplikacja_android.database.dao.UnitDao
import com.example.aplikacja_android.database.models.Igredient
import com.example.aplikacja_android.database.models.Recipe
import com.example.aplikacja_android.database.models.RecipeIgredientCrossRef
import com.example.aplikacja_android.database.models.Unit

class Repository(
    private val igredientDao: IgredientDao,
    private val recipeDao: RecipeDao,
    private val unitDao: UnitDao,
    private val recipeIgredientCrossRefDao: RecipeIgredientCrossRefDao
) {
    val recipes = recipeDao.getAllRecipes()
    val igredients = igredientDao.getAllIgredients()
    val units = unitDao.getAllUnits()
    fun getIgredientsOfRecipe(id:Int) = recipeIgredientCrossRefDao.getAllIgredientsOfRecipeWithUnits(id)
    fun getRecipesWithIgredient(id: Int) = recipeIgredientCrossRefDao.getAllRecipesWithIgredient(id)

    fun getSingleRecipe(id: Int): Recipe = recipeDao.getRecipe(id)
    suspend fun createRecipe(recipe: Recipe) : Long{
        return recipeDao.insert(recipe)
    }
    suspend fun updateRecipe(recipe: Recipe) = recipeDao.update(recipe)
    suspend fun deleteRecipe(recipe: Recipe) = recipeDao.delete(recipe)

    suspend fun createIgredient(igredient: Igredient) = igredientDao.insert(igredient)
    suspend fun updateIgredient(igredient: Igredient) = igredientDao.update(igredient)
    suspend fun deleteIgredient(igredient: Igredient) = igredientDao.delete(igredient)

    suspend fun createUnit(unit: Unit) = unitDao.insert(unit)

    suspend fun createRecipeIgredientCrossRef(recipeIgredientCrossRef: RecipeIgredientCrossRef) = recipeIgredientCrossRefDao.insert(recipeIgredientCrossRef)
    suspend fun updateRecipeIgredientCrossRef(recipeIgredientCrossRef: RecipeIgredientCrossRef) = recipeIgredientCrossRefDao.update(recipeIgredientCrossRef)
    suspend fun deleteRecipeIgredientCrossRef(recipeIgredientCrossRef: RecipeIgredientCrossRef) = recipeIgredientCrossRefDao.delete(recipeIgredientCrossRef)
}