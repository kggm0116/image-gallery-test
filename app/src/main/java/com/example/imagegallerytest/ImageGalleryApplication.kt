package com.example.imagegallerytest

import UserPreferencesRepository
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.imagegallerytest.data.datastore.UserPreferencesSerializer
import com.example.imagegallerytest.data.database.ImageGalleryDatabase
import com.example.imagegallerytest.data.repositories.FileRepository
import com.example.imagegallerytest.data.repositories.FolderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ImageGalleryApplication: Application() {

    companion object {
        private const val DATA_STORE_FILE_NAME = "user_preferences.proto"
    }

    private val Context.userPreferencesDataStore: DataStore<UserPreferences> by dataStore (
        fileName = DATA_STORE_FILE_NAME,
        serializer = UserPreferencesSerializer.SettingsSerializer
    )

    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { ImageGalleryDatabase.getDatabase(this, applicationScope) }
    val folderRepository by lazy { FolderRepository(database.folderDao()) }
    val fileRepository by lazy { FileRepository(database.fileDao()) }
    val userPreferencesRepository by lazy { UserPreferencesRepository(userPreferencesDataStore) }
}