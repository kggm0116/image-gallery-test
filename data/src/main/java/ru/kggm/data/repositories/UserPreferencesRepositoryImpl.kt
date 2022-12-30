package ru.kggm.data.repositories

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import ru.kggm.data.UserPreferencesData
import ru.kggm.domain.entities.UserPreferences
import ru.kggm.domain.repositories.UserPreferencesRepository

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<UserPreferencesData>
) : UserPreferencesRepository {

    override fun getPreferences() = dataStore.data
        .map {
            try {
                UserPreferences(
                    fileColumnCountLandscape = it.gridSizeFilesLandscape,
                    fileColumnCountPortrait = it.gridSizeFilesPortrait,
                    folderColumnCountLandscape = it.gridSizeFoldersLandscape,
                    folderColumnCountPortrait = it.gridSizeFoldersPortrait,
                    supportedFileExtensions = it.supportedFileExtensionsList
                )
            }
            catch (_: IllegalArgumentException) {
                val a = 1
                val defaultPreferences = UserPreferences.default
                setPreferences(defaultPreferences)
                defaultPreferences
            }
        }

        override suspend fun setPreferences(preference: UserPreferences) {
        dataStore.updateData {
            it.toBuilder()
                .setGridSizeFilesLandscape(preference.fileColumnCountLandscape)
                .setGridSizeFilesPortrait(preference.fileColumnCountPortrait)
                .setGridSizeFoldersLandscape(preference.folderColumnCountLandscape)
                .setGridSizeFoldersPortrait(preference.folderColumnCountPortrait)
                .clearSupportedFileExtensions()
                .addAllSupportedFileExtensions(preference.supportedFileExtensions)
                .build()
        }
    }
}