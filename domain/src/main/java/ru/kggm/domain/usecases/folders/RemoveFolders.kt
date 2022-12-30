package ru.kggm.domain.usecases.folders

import ru.kggm.domain.repositories.*
import ru.kggm.domain.usecases.files.GetFiles
import ru.kggm.domain.usecases.files.RemoveFiles
import ru.kggm.domain.utility.FlowExtensions.Companion.takeLast
import java.net.URI
import javax.inject.Inject

class RemoveFolders @Inject constructor(
    private val getFolders: GetFolders,
    private val removeFiles: RemoveFiles,
    private val getFiles: GetFiles,

    private val folderRepository: FolderRepository,
) {
    suspend operator fun invoke(vararg folderUris: URI) {
        getFolders().takeLast()
            .filter { it.uri in folderUris }
            .let { folders ->
                folderRepository.remove(folders)
                getFiles().takeLast()
                    .filter { it.folderUri in folderUris }
                    .also { removeFiles(*it.toTypedArray()) }
            }
    }
}