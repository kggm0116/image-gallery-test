package ru.kggm.imagegallerytest

import UserPreferencesRepository
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.kggm.imagegallerytest.data.database.ImageGalleryDatabase
import ru.kggm.imagegallerytest.data.database.datastore.UserPreferencesSerializer
import ru.kggm.imagegallerytest.data.repositories.FileRepository
import ru.kggm.imagegallerytest.data.repositories.FolderRepository

class ImageGalleryApplication: Application() {

    companion object {
        private const val DATA_STORE_FILE_NAME = "user_preferences.proto"
    }

    private val Context.userPreferencesDataStore: DataStore<ru.kggm.imagegallerytest.UserPreferences> by dataStore (
        fileName = ru.kggm.imagegallerytest.ImageGalleryApplication.Companion.DATA_STORE_FILE_NAME,
        serializer = UserPreferencesSerializer.SettingsSerializer
    )

    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { ImageGalleryDatabase.getDatabase(this, applicationScope) }
    val folderRepository by lazy { FolderRepository(database.folderDao()) }
    val fileRepository by lazy { FileRepository(database.fileDao()) }
    val userPreferencesRepository by lazy { UserPreferencesRepository(userPreferencesDataStore) }
}