package com.example.notes.navigation


sealed class Router(val route: String) {
    object NotesRouter : Router(route = "notes")

    data class NoteRouter(val id: String) : Router(route = "note/${id}") {
        companion object {
            const val TOKEN = "noteId"
            val route = NoteRouter("{$TOKEN}").route
            val newNoteRoute = NoteRouter("-1").route
        }
    }


}


