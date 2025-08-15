package com.example.notes.ui.screen.all_notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.notes.domain.model.Note
import com.example.notes.ui.screen.all_notes.components.NoteItem
import com.example.notes.ui.screen.all_notes.components.SortSelectionSection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllNoteScreen(
    viewModel: AllNotesScreenViewModel,
    navigateToEditNote: (note: Note?) -> Unit,
    deleteNote: (note: Note) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AllNotesTopBar(
                viewModel = viewModel,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navigateToEditNote(null) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = {
                    Text(
                        "New Note", style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                })
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            SortSelectionSection(
                vm = viewModel,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            )

            NotesBody(
                vm = viewModel,
                onNoteClick = navigateToEditNote,
                deleteNote = deleteNote,
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllNotesTopBar(
    viewModel: AllNotesScreenViewModel,
) {
    val notes by viewModel.notes.collectAsState()
    val notesCount = notes.size
    var showSearchBar by remember { mutableStateOf(false) }

    val searchFocusRequester = remember { FocusRequester() }

    LaunchedEffect(showSearchBar) {
        if (showSearchBar) {
            searchFocusRequester.requestFocus()
        }
    }

    TopAppBar(
        title = {
            if (showSearchBar) {
                Search2(
                    text = viewModel.searchText,
                    hint = "Search your Notes",
                    onValueChange = {
                        viewModel.setSearch(it)
                    },
                    onClose = {
                        val shouldClose = viewModel.searchText.isBlank()
                        viewModel.setSearch("")
                        if (shouldClose) {
                            searchFocusRequester.freeFocus()
                            showSearchBar = false
                        }
                    },
                    focusRequester = searchFocusRequester,
                )
            } else {
                Column {
                    Text(
                        text = "My Notes", style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "$notesCount ${if (notesCount == 1) "note" else "notes"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }, actions = {
            val isGridView = viewModel.isGridView

            if (!showSearchBar) {
                IconButton(onClick = {
                    showSearchBar = true
                }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search notes",
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.toggleGridView()
                    },
                ) {
                    Icon(
                        imageVector = if (isGridView) {
                            Icons.AutoMirrored.Filled.ViewList
                        } else {
                            Icons.Default.GridView
                        },
                        contentDescription = if (isGridView) "List View" else "Grid View"
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.toggleSortSection()
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Sort notes",
                        tint = if (viewModel.sortSectionCollapsed) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun Search2(
    text: String,
    onValueChange: (String) -> Unit,
    hint: String = "",
    contentTextStyle: TextStyle = TextStyle.Default,
    hintTextStyle: TextStyle = TextStyle.Default,
    shape: Shape = RoundedCornerShape(50),
    onClose: () -> Unit,
    focusRequester: FocusRequester? = null,
//    trailingIcon: @Composable (() -> Unit)? = null,
//    singleLine: Boolean = true,
) {
    Surface {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().apply {
                focusRequester?.let {
                    this.focusRequester(focusRequester)
                }
            },
            value = text,
            shape = shape,
            onValueChange = onValueChange,
            textStyle = contentTextStyle,
            trailingIcon = {
                Surface(
                    modifier = Modifier.clickable {
                        onClose()
                    }) {
                    Icon(
                        Icons.Default.Close,
                        "Text Bar Icon",
                    )
                }
            },
//            singleLine = singleLine,
            placeholder = {
                Text(
                    hint,
                    style = hintTextStyle,
//                color = DarkGray
                )
            },
//            colors =
//            TextFieldDefaults.textFieldColors(
//                containerColor = Color.Transparent,
////            textColor = Color.Black,
////            disabledTextColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent,
//            )
        )
    }

}

@Composable
fun NotesBody(
    vm: AllNotesScreenViewModel,
    onNoteClick: (Note) -> Unit,
    deleteNote: (note: Note) -> Unit,
) {
    val notes by vm.notes.collectAsState()
    if (notes.isEmpty()) {
        EmptyNotes(modifier = Modifier.fillMaxSize())
    } else {
        if (vm.isGridView) {
            NotesGrid(
                vm = vm,
                onNoteClick = onNoteClick,
                deleteNote = deleteNote,
            )
        } else {
            NotesColumn(
                vm = vm,
                onNoteClick = onNoteClick,
                deleteNote = deleteNote,
            )
        }
    }
}

@Composable
fun NotesGrid(
    vm: AllNotesScreenViewModel,
    onNoteClick: (Note) -> Unit,
    deleteNote: (note: Note) -> Unit,
) {
    val notes by vm.notes.collectAsState()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
        modifier = Modifier.fillMaxSize()
    ) {
        items(notes) { note ->
            NoteItem(
                vm = vm,
                note = note,
                onNoteClick = {
                    onNoteClick(note)
                },
                deleteNote = deleteNote,
            )
        }
    }
}

@Composable
fun NotesColumn(
    vm: AllNotesScreenViewModel,
    onNoteClick: (Note) -> Unit,
    deleteNote: (note: Note) -> Unit,
) {
    val notes by vm.notes.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(notes) { note ->
            NoteItem(
                vm = vm,
                note = note,
                onNoteClick = {
                    onNoteClick(note)
                },
                deleteNote = deleteNote
            )
        }
    }
}

@Composable
fun EmptyNotes(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(48.dp),
        ) {
            Box(
                modifier = Modifier.size(120.dp).background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f)
                        )
                    ), shape = CircleShape
                ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notes,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "No notes yet",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tap the + button to create your first note",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}