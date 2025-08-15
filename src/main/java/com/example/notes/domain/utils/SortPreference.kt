package com.example.notes.domain.utils

import com.example.notes.domain.model.Note
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.comparisons.compareBy


enum class SortBy {
    Time,
    Color,
    Title
}

enum class SortOrder {
    Ascending,
    Descending
}

@Serializable
data class SortPreference(
    val sortBy: SortBy,
    val sortOrder: SortOrder,
    val hasSampleNotesBeenAdded: Boolean = false,
) {
    companion object {
        val DEFAULT = SortPreference(
            sortBy = SortBy.Time,
            sortOrder = SortOrder.Descending,
            hasSampleNotesBeenAdded = false,
        )
    }

    val comparator: Comparator<Note>
        get() {
            val baseComparator = when (sortBy) {
                SortBy.Time -> compareBy<Note> { it.timestamp }
                SortBy.Color -> compareBy { it.color.ordinal }
                SortBy.Title -> compareBy { it.title }
            }

            return if (sortOrder == SortOrder.Descending) {
                baseComparator.reversed()
            } else {
                baseComparator
            }
        }

}