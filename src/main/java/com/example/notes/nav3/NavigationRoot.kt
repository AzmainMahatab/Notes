package com.example.notes.nav3

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.composeext.composables.dialoge.ConfirmationDialogContent
import com.example.composeext.composables.dialoge.defaultDialog
import com.example.composeext.composables.dialoge.leftToRightScreenTransition
import com.example.composeext.interaction.Action
import com.example.composeext.interaction.InteractiveScaffold
import com.example.composeext.nav3.rememberBackStack
import com.example.notes.domain.model.Note
import com.example.notes.ui.screen.all_notes.AllNoteScreen
import com.example.notes.ui.screen.all_notes.AllNotesScreenViewModel
import com.example.notes.ui.screen.single_note.SingleNoteScreen
import com.example.notes.ui.screen.single_note.SingleNoteScreenViewModel
import kotlinx.serialization.Serializable
import org.example.std_plus.collection.keepFirstAndTrim
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.scope.KoinScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

sealed interface Page : NavKey {
    @Serializable
    data object NoteList : Page

    @Serializable
    data class ConfirmationDelete(val note: Note) : Page

    @Serializable
//    data class Note(val id: Int) : Page
    data class SingleNote(val note: Note?) : Page
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberBackStack<Page>(Page.NoteList)
    val back = { backStack.removeLastOrNull() }
    val backToHomePage = {
        backStack.keepFirstAndTrim(1)
    }

    val delete = { it: Note ->
        backStack.add(Page.ConfirmationDelete(it))
    }

    val allNotesScreenViewModel = koinViewModel<AllNotesScreenViewModel>()

    InteractiveScaffold { interactionController ->
        NavDisplay(
            modifier = modifier,
            backStack = backStack,
            entryDecorators = listOf(
                // Add the default decorators for managing scenes and saving state
                rememberSceneSetupNavEntryDecorator(), rememberSavedStateNavEntryDecorator(),
                // Then add the view model store decorator
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = { page ->
                when (page) {
                    Page.NoteList -> NavEntry(page) {
                        AllNoteScreen(
                            viewModel = allNotesScreenViewModel,
                            navigateToEditNote = {
                                backStack.add(Page.SingleNote(it))
                            },
                            deleteNote = {
                                delete(it)
                            },
                        )
                    }

                    is Page.ConfirmationDelete -> NavEntry(
                        page,
                        metadata = defaultDialog,
                    ) {
                        val note = page.note
                        ConfirmationDialogContent(
                            title = "Delete Note",
                            message = "Are you sure you want to delete \"${note.title.ifEmpty { "Untitled" }}\"? This action cannot be undone.",
                            confirmText = "Delete",
                            dismissText = "Cancel",
                            onConfirm = {
                                allNotesScreenViewModel.deleteNote(note)
                                backToHomePage()
                                interactionController.showSnackbar(
                                    "Note deleted", Action("Undo") {
                                        allNotesScreenViewModel.restoreNote()
                                    }, duration = SnackbarDuration.Short
                                )
                            },
                            onDismiss = {
                                back()
                            },
                        )
                    }

                    is Page.SingleNote -> NavEntry(
                        page,
                        metadata = leftToRightScreenTransition,
                    ) {
                        KoinScope(
                            scopeDefinition = {
                                createScope<Page.SingleNote>()
                            },
                        ) {
                            val viewModel = koinViewModel<SingleNoteScreenViewModel>(
                                parameters = {
                                    parametersOf(page.note)
                                },
                            )

                            SingleNoteScreen(
                                viewModel = viewModel,
                                navigateBack = {
                                    it?.let {
                                        allNotesScreenViewModel.fxss(it)
                                    }
                                    back()
                                },

                                deleteNote = {
                                    delete(it)
                                },
                            )
                        }
                    }
                }
            },
        )
    }
}

sealed interface NoteOperation {
    data class Save(val note: Note) : NoteOperation
    data class Delete(val note: Note) : NoteOperation
}