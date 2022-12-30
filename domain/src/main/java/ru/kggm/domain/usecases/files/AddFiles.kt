package ru.kggm.domain.usecases.files

import kotlinx.coroutines.flow.first
import ru.kggm.domain.entities.*
import ru.kggm.domain.repositories.*
import ru.kggm.domain.usecases.folders.UpdateFolderFileCount
import ru.kggm.domain.usecases.preferences.GetUserPreferences
import ru.kggm.domain.utility.UriExtensions.Companion.getExtension
import ru.kggm.domain.utility.UriExtensions.Companion.getName
import java.net.URI
import javax.inject.Inject

class AddFiles @Inject constructor (
    private val getUserPreferences: GetUserPreferences,
    private val updateFolderFileCount: UpdateFolderFileCount,

    private val fileRepository: FileRepository,
) {
    suspend operator fun invoke(folderUri: URI, vararg fileUris: URI) {
        fileUris
            .filter {
                it.getExtension() in getUserPreferences().first().supportedFileExtensions
            }
            .map { File(
                uri = it,
                folderUri = folderUri,
                name = it.getName(),
                extension = it.getExtension(),
            )}
            .also {
                fileRepository.add(it)
                updateFolderFileCount(folderUri)
            }
    }
}