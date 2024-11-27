package com.example.aplikacja_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacja_android.database.models.Note

@Dao
interface NoteDao {

    // Insert Note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    // Delete Note
    @Delete
    suspend fun deleteNote(note: Note)

    // Query all notes for a specific recipe by recipeId
    @Query("SELECT * FROM Notes WHERE recipeId = :recipeId")
    fun getNotesByRecipeId(recipeId: Int): LiveData<List<Note>>
}