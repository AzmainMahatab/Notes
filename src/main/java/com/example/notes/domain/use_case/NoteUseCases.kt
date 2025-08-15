package com.example.notes.domain.use_case

import com.example.notes.domain.model.Note
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.domain.utils.SortPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


class NoteUseCases(
    private val repository: NoteRepository,
) {

    fun getFilteredNotesByFlowOrder(
        searchPrompt: Flow<String>,
        sortPreference: Flow<SortPreference>,
    ): Flow<List<Note>> = combine(
        sortPreference,
        repository.getAllNotes(),
        searchPrompt,
    ) { order, notesList, text ->

        val searchFor = text.lowercase()

        val allNotes = notesList.filter {
            text.isBlank() || it.title.lowercase()
                .contains(searchFor) || it.content.lowercase()
                .contains(searchFor)
        }

        allNotes.sortedWith(order.comparator)
    }

    suspend fun deleteNote(note: Note) {
        repository.deleteNote(note)
    }

    suspend fun addNote(note: Note) {
        repository.upsertNote(note)
    }

    suspend fun getNoteById(id: Int): Note? = repository.getNote(id)

    suspend fun initializeSampleNotes() {
        val sampleNotes = createSampleNotes()
        sampleNotes.forEach { note ->
            repository.upsertNote(note)
        }
    }

    private fun createSampleNotes(): List<Note> {
        return listOf(
            Note(
                title = "Welcome to Notes! 📝",
                content = "This is your first note! You can:\n\n• Create new notes by tapping the + button\n• Edit notes by tapping on them\n• Change colors by selecting from the color palette\n• Delete notes with the delete button\n• Sort and search your notes\n\nHappy note-taking!",
                color = com.example.notes.ui.theme.NoteColor.SKY
            ),
            Note(
                title = "Ideas & Inspiration 💡",
                content = "Use this space to capture your brilliant ideas:\n\n• App improvements\n• Book recommendations\n• Travel destinations\n• Creative projects\n• Random thoughts\n\nGreat ideas often come unexpectedly - make sure to write them down!",
                color = com.example.notes.ui.theme.NoteColor.LEMON
            ),
            Note(
                title = "Daily Tasks ✅",
                content = "Keep track of what you need to do:\n\n• Buy groceries\n• Call dentist\n• Finish project report\n• Exercise for 30 minutes\n• Read for 20 minutes\n\nTip: Check off completed tasks for satisfaction!",
                color = com.example.notes.ui.theme.NoteColor.MINT
            ),
            Note(
                title = "Goals & Dreams 🎯",
                content = "Write down your aspirations:\n\n• Learn a new language\n• Travel to Japan\n• Start a side project\n• Read 24 books this year\n• Learn to play guitar\n\nWriting goals down makes them more likely to happen!",
                color = com.example.notes.ui.theme.NoteColor.LAVENDER
            ),
            Note(
                title = "Recipe: Quick Pasta 🍝",
                content = "Simple and delicious pasta recipe:\n\nIngredients:\n• 200g pasta\n• 2 cloves garlic\n• Olive oil\n• Parmesan cheese\n• Salt & pepper\n\nInstructions:\n1. Cook pasta according to package\n2. Sauté garlic in olive oil\n3. Mix pasta with garlic oil\n4. Add cheese and season\n\nReady in 15 minutes!",
                color = com.example.notes.ui.theme.NoteColor.PEACH
            )
        )
    }

}
