package com.example.notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.notes.ui.theme.NoteColor
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Instant

@Entity
@Serializable
data class Note(
    val title: String,
    val content: String,
    val color: NoteColor,
    val timestamp: Instant = Clock.System.now(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) {
    fun userContentIsDifferentFrom(other: Note) =
        title != other.title || content != other.content || color != other.color
}

class Converters {
    @TypeConverter
    fun fromInstant(instant: Instant) = instant.toEpochMilliseconds()

    @TypeConverter
    fun toInstant(long: Long) = Instant.fromEpochMilliseconds(long)
}