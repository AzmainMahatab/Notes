package com.example.notes.ui.screen.all_notes

import com.example.notes.domain.model.Note
import com.example.notes.domain.utils.SortPreference

data class AllNoteScreenState(
    val notes: List<Note> = emptyList(),
    val sortPreference: SortPreference = SortPreference.DEFAULT,
    val isOrderSectionVisible: Boolean = false,
)