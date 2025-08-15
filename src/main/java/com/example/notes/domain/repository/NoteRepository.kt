package com.example.notes.domain.repository

import com.example.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(): Flow<List<Note>>

    suspend fun upsertNote(note: Note)

    suspend fun getNote(id: Int): Note?

    fun getNoteFlow(id: Int): Flow<Note?>

    suspend fun deleteNote(note: Note)

    suspend fun deleteNoteById(id: Int)

    suspend fun deleteAllNotes()
}