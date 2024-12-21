package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacja_android.database.models.RecipeTags

@Dao
interface RecipeTagsDao {
    @Query("SELECT * FROM recipe_tags WHERE recipeId = :recipeId")
    fun getTagsByRecipeId(recipeId: Int): LiveData<List<RecipeTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: RecipeTags)

    @Delete
    suspend fun deleteTag(tag: RecipeTags)
}