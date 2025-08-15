package com.example.notes.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * from note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * from note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("SELECT * FROM note WHERE id = :id")
    fun getNoteFlowById(id: Int): Flow<Note?>


    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

    @Query("DELETE FROM note")
    suspend fun deleteAllNotes()

    @Delete
    suspend fun deleteNotes(vararg note: Note)
}