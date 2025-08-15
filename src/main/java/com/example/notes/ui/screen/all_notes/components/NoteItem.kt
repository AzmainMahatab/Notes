package com.example.notes.ui.screen.all_notes.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.composeext.interaction.LocalInteractionController
import com.example.notes.domain.model.Note
import com.example.notes.domain.utils.DateUtils
import com.example.notes.ui.screen.all_notes.AllNotesScreenViewModel
import kotlinx.coroutines.delay
import kotlin.time.Instant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    vm: AllNotesScreenViewModel,
    onNoteClick: () -> Unit = {},
    deleteNote: (note: Note) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = 400f),
        label = "scale"
    )

    val animatedColor by animateColorAsState(
        targetValue = note.color.themedColor(),
        animationSpec = spring(stiffness = 300f),
        label = "color"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onNoteClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = animatedColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 2.dp else 8.dp,
            pressedElevation = 2.dp,
            hoveredElevation = 12.dp
        )

    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            NoteItemHeader(
                vm = vm,
                note = note,
                deleteNote = deleteNote
            )

            if (note.content.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                NoteItemContent(content = note.content)
            }

            Spacer(modifier = Modifier.height(16.dp))

            NoteItemFooter(
                timestamp = note.timestamp, content = note.content, title = note.title
            )
        }
    }
}

@Composable
fun NoteItemHeader(
    vm: AllNotesScreenViewModel,
    note: Note,
    deleteNote: (note: Note) -> Unit,
) {
    val interactions = LocalInteractionController.current
    val title = note.title
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = title.ifEmpty { "Untitled" },
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            modifier = Modifier.size(32.dp),
            onClick = {
                deleteNote(note)
//                interactions.showSimpleDialogue(
//                    SimpleDialog.Confirmation(
//                        title = "Delete Note",
//                        content = "Are you sure you want to delete \"${title.ifEmpty { "Untitled" }}\"? This action cannot be undone.",
//                        confirmText = "Delete",
//                        dismissText = "Cancel",
//                        onConfirm = {
//                            vm.deleteNote(note)
//                            interactions.showSnackbar(
//                                "Note deleted",
//                                Action("Undo") {
//                                    vm.restoreNote()
//                                }
//                            )
//                        })
//                )
            },
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete note",
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun NoteItemContent(content: String) {
    Text(
        text = content,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun NoteItemFooter(
    timestamp: Instant,
    content: String,
    title: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = DateUtils.durationToText(timestamp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            // Word count indicator
            NoteIndicatorChip(content)

            // Priority indicator
            if (title.contains(Regex("important|urgent|priority", RegexOption.IGNORE_CASE))) {
                Icon(
                    imageVector = Icons.Default.PriorityHigh,
                    contentDescription = "High priority",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun String.wordCount(): Int = split(" ").size

@Composable
private fun NoteIndicatorChip(content: String) {
    val wordCount = content.wordCount()
    if (wordCount != 0) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ) {
            Text(
                text = "$wordCount words",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    horizontal = 8.dp, vertical = 4.dp
                )
            )
        }
    }
}