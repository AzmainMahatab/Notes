package com.example.notes.ui.screen.single_note

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.notes.domain.model.Note
import com.example.notes.domain.utils.DateUtils
import com.example.notes.nav3.NoteOperation
import com.example.notes.ui.components.ColorPickerSection
import com.example.notes.ui.components.ModernTextField
import com.example.notes.ui.theme.NoteColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleNoteScreen(
    viewModel: SingleNoteScreenViewModel,
    navigateBack: (NoteOperation?) -> Unit,
    deleteNote: (note: Note) -> Unit,
) {

    val noteColors = NoteColor.themedColors()
    val keyboardController = LocalSoftwareKeyboardController.current

    val titleFocusRequester = remember { FocusRequester() }
    val contentFocusRequester = remember { FocusRequester() }

    val note = viewModel.note

    BackHandler {
        navigateBack(viewModel.pendingEdit())
    }

    // Auto-focus for new notes
    LaunchedEffect(note) {
        if (note == null) {
            contentFocusRequester.requestFocus()
        }
    }

    val themedColor = viewModel.color.themedColor()

    // Background color animation
    val animatedBackgroundColor by animateColorAsState(
        targetValue = themedColor,
        animationSpec = tween(durationMillis = 500),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SingleNoteTopBar(
                viewModel = viewModel,
                onBackClick = {
                    navigateBack(viewModel.pendingEdit())
                },
                deleteNote = deleteNote,
            )
        },
        containerColor = animatedBackgroundColor.copy(alpha = 0.1f),
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            // Color Picker Section
            AnimatedVisibility(
                visible = viewModel.colorPickerIsCollapsed,
                enter = slideInVertically(spring(Spring.DampingRatioMediumBouncy)) + fadeIn(),
                exit = slideOutVertically(spring(Spring.DampingRatioMediumBouncy)) + fadeOut()
            ) {
                ColorPickerSection(
                    selectedColor = themedColor,
                    colors = noteColors,
                    onColorSelected = { colorIndex ->
                        viewModel.setColor(NoteColor.byIndex(colorIndex))
                    },
                )
            }

            // Note
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {

                // Title
                ModernTextField(
                    value = viewModel.titleText,
                    onValueChange = {
                        viewModel.setTitle(it)
                    },
                    placeholder = "Note title",
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    singleLine = true,
                    gotFocused = {
                        viewModel.focusTitle()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(titleFocusRequester),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            // Move focus to content field when Next is pressed
                            contentFocusRequester.requestFocus()
                        }
                    ),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Content
                ModernTextField(
                    value = viewModel.contentText,
                    onValueChange = {
                        viewModel.setContent(it)
                    },
                    placeholder = "Start writing...",
                    textStyle = MaterialTheme.typography.bodyLarge,
                    gotFocused = {
                        viewModel.focusContent()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .focusRequester(contentFocusRequester),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Hide keyboard when Done is pressed
                            keyboardController?.hide()
                        }
                    ),
                )
            }
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleNoteTopBar(
    viewModel: SingleNoteScreenViewModel,
    onBackClick: () -> Unit,
    deleteNote: (note: Note) -> Unit,
) {
    val note = viewModel.note
    val lastModified = note?.timestamp

    TopAppBar(
        title = {
            Column {
                Text(
                    text = lastModified?.let { "Edit Note" } ?: "New Note",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
                lastModified?.let {
                    Text(
                        text = "Last modified ${DateUtils.durationToText(lastModified)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }, navigationIcon = {
            IconButton(
                onClick = {
                    onBackClick()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }, actions = {
            // ColorPicker Button
            IconButton(
                onClick = {
                    viewModel.toggleColorPicker()
//                    showColorPicker = !showColorPicker
                }) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "Change color",
                    tint = if (viewModel.colorPickerIsCollapsed) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
            var isOptionMenuExpanded by remember { mutableStateOf(false) }
            // MoreOptions Button
            IconButton(
                onClick = {
                    isOptionMenuExpanded = !isOptionMenuExpanded
                },
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                )
            }

            // More options dropdown
            DropdownMenu(
                expanded = isOptionMenuExpanded,
                onDismissRequest = { isOptionMenuExpanded = false },
            ) {
                lastModified?.let {
                    DropdownMenuItem(
                        text = {
                            Text("Delete")
                        },
                        onClick = {
                            isOptionMenuExpanded = false
                            deleteNote(note)
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                            )
                        },
                    )
                }
                DropdownMenuItem(text = { Text("Share") }, onClick = {
                    isOptionMenuExpanded = false
                    // TODO: Implement sharing
                }, leadingIcon = {
                    Icon(Icons.Default.Share, contentDescription = null)
                })
            }

//                TODO: delete this button
            // Save button
//            IconButton(onClick = {
//                viewModel.saveNote2x()
//            }) {
//                Icon(
//                    imageVector = Icons.Default.Check,
//                    contentDescription = "Save note",
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
