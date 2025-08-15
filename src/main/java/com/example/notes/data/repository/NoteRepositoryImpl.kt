package com.example.notes.data.repository

import com.example.notes.data.data_source.NoteDao
import com.example.notes.domain.model.Note
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> {
        //TODO
        return noteDao.getAllNotes().flowOn(Dispatchers.IO)
    }

    override suspend fun upsertNote(note: Note) = withContext(Dispatchers.IO) {
        noteDao.upsertNote(note)
    }


    override suspend fun deleteNote(note: Note) = withContext(Dispatchers.IO) {
        noteDao.deleteNote(note)
    }

    override suspend fun getNote(id: Int): Note? = withContext(Dispatchers.IO) {
        noteDao.getNoteById(id)
    }

    override fun getNoteFlow(id: Int): Flow<Note?> = noteDao.getNoteFlowById(id).flowOn(Dispatchers.IO)

    override suspend fun deleteNoteById(id: Int) = withContext(Dispatchers.IO) {
        noteDao.deleteNoteById(id)
    }

    override suspend fun deleteAllNotes() = withContext(Dispatchers.IO) {
        noteDao.deleteAllNotes()
    }
}