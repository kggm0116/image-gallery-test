package ru.kggm.domain.usecases.folders

import ru.kggm.domain.entities.Folder
import ru.kggm.domain.repositories.*
import ru.kggm.domain.usecases.files.GetFiles
import ru.kggm.domain.utility.FlowExtensions.Companion.takeLast
import java.net.URI
import javax.inject.Inject

class UpdateFolderFileCount @Inject constructor(
    private val getFolders: GetFolders,
    private val getFiles: GetFiles,

    private val folderRepository: FolderRepository
) {

    internal suspend operator fun invoke(vararg folders: Folder) {
        val files = getFiles().takeLast()
        folders
            .map { folder ->
                folder.copy(fileCount = files.count { it.folderUri == folder.uri })
            }
            .also {
                folderRepository.update(it)
            }
    }

    suspend operator fun invoke(vararg folderUris: URI) {
        val files = getFiles().takeLast()
        val allFolders = getFolders().takeLast()
        val relevantFolders = allFolders
            .filter { it.uri in folderUris }
            .ifEmpty { allFolders }

        for (folder in relevantFolders)
            folderRepository.update(folder.copy(
                fileCount = files.count { it.folderUri == folder.uri }
            ))
    }
}