package com.example.notes.ui.screen.single_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.composeext.savable.saveable2
import com.example.notes.domain.model.Note
import com.example.notes.nav3.NoteOperation
import com.example.notes.ui.theme.NoteColor
import kotlinx.coroutines.FlowPreview


@OptIn(SavedStateHandleSaveableApi::class, FlowPreview::class)
class SingleNoteScreenViewModel(
    note: Note? = null,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    //UI
    var note by mutableStateOf(note)

    var titleText by savedStateHandle.saveable {
        mutableStateOf(note?.title ?: "")
    }
        private set

    fun setTitle(title: String) {
        titleText = title
    }

    var contentText by savedStateHandle.saveable {
        mutableStateOf(note?.content ?: "")
    }
        private set

    fun setContent(content: String) {
        contentText = content
    }

    enum class Focused {
        TITLE,
        CONTENT
    }

    var focused by savedStateHandle.saveable {
        mutableStateOf(Focused.CONTENT)
    }
        private set

    fun focusTitle() {
        focused = Focused.TITLE
    }

    fun focusContent() {
        focused = Focused.CONTENT
    }


    private var _color: NoteColor by savedStateHandle.saveable2(
        note?.id?.toString(),
    ) {
        mutableStateOf(note?.color ?: NoteColor.random())
    }

    val color
        get() = _color

    fun setColor(newColor: NoteColor) {
        _color = newColor
    }

    var colorPickerIsCollapsed by mutableStateOf(false)
        private set

    fun toggleColorPicker() {
        colorPickerIsCollapsed = !colorPickerIsCollapsed
    }

    private fun newContentIsNotEmpty(): Boolean =
        (titleText.isNotBlank() || contentText.isNotBlank())

    private fun newContentIsDifferent() =
        note?.title != titleText || note?.content != contentText || note?.color != color

    fun pendingEdit(): NoteOperation? {
        return if (newContentIsDifferent()) {
            if (newContentIsNotEmpty()) {
                val newNote = Note(
                    title = titleText,
                    content = contentText,
                    color = color,
                    id = note?.id ?: 0, // 0 means new
                )
                note = newNote
                NoteOperation.Save(newNote)
            } else {
                NoteOperation.Delete(note!!)
            }
        } else null
    }
}

