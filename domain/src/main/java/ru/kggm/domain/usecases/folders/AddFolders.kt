package ru.kggm.domain.usecases.folders

import ru.kggm.domain.entities.*
import ru.kggm.domain.repositories.*
import java.net.URI
import javax.inject.Inject

class AddFolders @Inject constructor (
    private val updateFolderFileCount: UpdateFolderFileCount,
    private val folderRepository: FolderRepository
) {
    suspend operator fun invoke(vararg folderUris: URI) {
        folderUris
            .map { Folder.fromUri(it) }
            .let {
                folderRepository.add(it)
                updateFolderFileCount(*it.toTypedArray())
            }
    }
}