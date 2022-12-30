package ru.kggm.domain.usecases.files

import kotlinx.coroutines.flow.first
import ru.kggm.domain.usecases.folders.UpdateFolderFileCount
import java.net.URI
import javax.inject.Inject

class SetFilesForFolder @Inject constructor (
    private val getFilesForFolder: GetFilesForFolder,
    private val addFiles: AddFiles,
    private val removeFiles: RemoveFiles,
    private val updateFolderFileCount: UpdateFolderFileCount
) {
    suspend operator fun invoke(folderUri: URI, newFileUris: List<URI>) {
        val oldFileUris = getFilesForFolder(folderUri)
            .first()
            .map { it.uri }
        val filesToAdd = newFileUris.subtract(oldFileUris.toSet())
        val filesToRemove = oldFileUris.subtract(newFileUris.toSet())
        addFiles(folderUri, *filesToAdd.toTypedArray())
        removeFiles(folderUri, *filesToRemove.toTypedArray())
        updateFolderFileCount(folderUri)
    }
}