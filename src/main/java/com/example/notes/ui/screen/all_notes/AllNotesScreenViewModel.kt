package com.example.notes.ui.screen.all_notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.notes.domain.model.Note
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.domain.utils.SortPreference
import com.example.notes.nav3.NoteOperation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


const val DEFAULT_TIMEOUT: Long = 5000

@OptIn(SavedStateHandleSaveableApi::class)
class AllNotesScreenViewModel(
//    private val noteUseCases: NoteUseCases,
    private val repository: NoteRepository,
    private val dataStore: DataStore<SortPreference>,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    //UI fields
    val sortPreference = dataStore.data.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(DEFAULT_TIMEOUT),
        initialValue = SortPreference.DEFAULT,
    )

    fun selectSort(sortPreference: SortPreference) {
        viewModelScope.launch {
            dataStore.updateData {
                sortPreference
            }
        }
    }

    //TODO: Save preference in datastore
    var isGridView by savedStateHandle.saveable { mutableStateOf(false) }
        private set

    fun toggleGridView() {
        isGridView = !isGridView
    }

    var sortSectionCollapsed by mutableStateOf(false)
        private set

    fun toggleSortSection() {
        sortSectionCollapsed = !sortSectionCollapsed
    }

    var searchText by savedStateHandle.saveable { mutableStateOf("") }
        private set

    fun setSearch(text: String) {
        searchText = text
    }

//    fun searchClose() {
//        val shouldClose = searchText.isBlank()
//        setSearch("")
////        sortSectionCollapsed = false
//
////        if (shouldClose) {
////            sortSectionCollapsed = false
////        }
//    }


    //Business View
    val notes: StateFlow<List<Note>> = getFilteredNotesByFlowOrder().stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = emptyList(),
    )


    private fun getFilteredNotesByFlowOrder(): Flow<List<Note>> = combine(
        sortPreference,
        repository.getAllNotes(),
        snapshotFlow { searchText },
    ) { order, notesList, text ->

        val searchFor = text.lowercase()

        val allNotes = notesList.filter {
            text.isBlank() || it.title.lowercase().contains(searchFor) || it.content.lowercase().contains(searchFor)
        }

        allNotes.sortedWith(order.comparator)
    }


    //Business Op
    private var recentlyDeleted: Note? = null
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            recentlyDeleted = note
            repository.deleteNote(note)
        }
    }

    fun upsertNote(note: Note) {
        viewModelScope.launch {
            repository.upsertNote(note)
        }
    }

//    suspend fun deleteNote(noteId: Int) {
//        viewModelScope.launch {
//
//        }
//        recentlyDeleted = note
//        repository.deleteNote(note)
//    }

    fun fxss(noteOperation: NoteOperation) {
        when (noteOperation) {
            is NoteOperation.Save -> {
                upsertNote(noteOperation.note)
            }

            is NoteOperation.Delete -> {
                deleteNote(noteOperation.note)
            }
        }
    }

    fun restoreNote() {
        viewModelScope.launch {
            recentlyDeleted?.let {
                repository.upsertNote(it)
                recentlyDeleted = null
            }
        }
    }


}

