package com.example.notes.domain.use_case

import androidx.datastore.core.DataStore
import com.example.notes.domain.utils.SortPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppInitializer(
    private val noteUseCases: NoteUseCases,
    private val dataStore: DataStore<SortPreference>,
    private val scope: CoroutineScope,
) {

    fun initialize() {
        scope.launch {
            initializeSampleNotesIfNeeded()
        }
    }

    private suspend fun initializeSampleNotesIfNeeded() {
        val currentSettings = dataStore.data.first()

        if (!currentSettings.hasSampleNotesBeenAdded) {
            // Add sample notes on first launch
            noteUseCases.initializeSampleNotes()

            // Update settings to mark that sample notes have been added
            dataStore.updateData { currentPreferences ->
                currentPreferences.copy(hasSampleNotesBeenAdded = true)
            }
        }
    }
}
