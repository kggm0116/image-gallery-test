package ru.kggm.domain.usecases.preferences

import ru.kggm.domain.entities.File
import ru.kggm.domain.entities.UserPreferences
import ru.kggm.domain.repositories.*
import ru.kggm.domain.utility.FlowExtensions.Companion.takeLast
import javax.inject.Inject

class UpdateUserPreferences @Inject constructor(
    private val getUserPreferences: GetUserPreferences,

    private val userPreferencesRepository: UserPreferencesRepository,
    private val removeFilesWithInvalidExtension: RemoveFilesWithInvalidExtension
) {
    suspend fun setFolderColumnCountLandscape(value: Int) =
        getUserPreferences().takeLast()
            .copy( folderColumnCountLandscape = value.coerceIn(
                UserPreferences.MIN_FOLDER_COLUMN_COUNT_LANDSCAPE..UserPreferences.MAX_FOLDER_COLUMN_COUNT_LANDSCAPE)
            )
            .also { userPreferencesRepository.setPreferences(it) }

    suspend fun setFolderColumnCountPortrait(value: Int) =
        getUserPreferences().takeLast()
            .copy( folderColumnCountPortrait = value.coerceIn(
                UserPreferences.MIN_FOLDER_COLUMN_COUNT_PORTRAIT..UserPreferences.MAX_FOLDER_COLUMN_COUNT_PORTRAIT)
            )
            .also { userPreferencesRepository.setPreferences(it) }

    suspend fun setFileColumnCountLandscape(value: Int) =
        getUserPreferences().takeLast()
            .copy( fileColumnCountLandscape = value.coerceIn(
                UserPreferences.MIN_FILE_COLUMN_COUNT_LANDSCAPE..UserPreferences.MAX_FILE_COLUMN_COUNT_LANDSCAPE)
            )
            .also { userPreferencesRepository.setPreferences(it) }

    suspend fun setFileColumnCountPortrait(value: Int) =
        getUserPreferences().takeLast()
            .copy( fileColumnCountPortrait = value.coerceIn(
                UserPreferences.MIN_FILE_COLUMN_COUNT_PORTRAIT..UserPreferences.MAX_FILE_COLUMN_COUNT_PORTRAIT)
            )
            .also { userPreferencesRepository.setPreferences(it) }

    suspend fun setSupportedFileExtensions(value: List<String>) =
        getUserPreferences().takeLast()
            .copy(supportedFileExtensions =
                if (value.all { File.REGEX_SUPPORTED_FILE_EXTENSION.matches(it) })
                    value
                else
                    UserPreferences.DEFAULT_SUPPORTED_FILE_EXTENSIONS
            )
            .also {
                userPreferencesRepository.setPreferences(it)
                removeFilesWithInvalidExtension(it.supportedFileExtensions)
            }
}