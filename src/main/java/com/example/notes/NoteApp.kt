package com.example.notes

import android.app.Application
import android.content.Context
import androidx.datastore.dataStore
import com.example.notes.data.data_source.NoteDatabase
import com.example.notes.data.repository.NoteRepositoryImpl
import com.example.notes.domain.model.Note
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.domain.use_case.AppInitializer
import com.example.notes.domain.use_case.NoteUseCases
import com.example.notes.domain.utils.AppSettingsSerializer
import com.example.notes.nav3.Page
import com.example.notes.ui.screen.all_notes.AllNotesScreenViewModel
import com.example.notes.ui.screen.single_note.SingleNoteScreenViewModel
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val Context.dataStore by dataStore("app-settings.pb", AppSettingsSerializer)

class NoteApp : Application() {
    private val appModule = module {

        single {
            SupervisorJob()
        }

        single {
            val job: CompletableJob = get()
            CoroutineScope(Dispatchers.IO + job)
        }

        // Singletons
        single {
            NoteDatabase.getInstance(
                androidContext()
            )
        }

        single {
            get<NoteDatabase>().noteDao
        }

        singleOf(::NoteRepositoryImpl) {
            bind<NoteRepository>()
        }

        singleOf(::NoteUseCases)

        single {
            androidContext().dataStore
        }

        // App initialization service
        single {
            AppInitializer(
                noteUseCases = get(),
                dataStore = get(),
                scope = get()
            )
        }

        // ViewModels
        viewModelOf(::AllNotesScreenViewModel)

        scope<Page.SingleNote> {
            viewModel { (note: Note?) ->
                SingleNoteScreenViewModel(
                    note = note,
                    savedStateHandle = get(),
                )
            }
        }

    }


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NoteApp)
            androidLogger()
            modules(appModule)
        }

        // Initialize app (including sample notes on first launch)
        val appInitializer = get<AppInitializer>()
        appInitializer.initialize()
    }
}