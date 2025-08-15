package com.example.notes.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notes.domain.model.Converters
import com.example.notes.domain.model.Note
import kotlinx.coroutines.Dispatchers

@Database(entities = [Note::class], version = 1)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val NAME = "notes_db"

        fun getInstance(context: Context) =
            Room.databaseBuilder<NoteDatabase>(
                context,
                NAME,
            ).setQueryCoroutineContext(Dispatchers.IO)
                .build()
    }
}