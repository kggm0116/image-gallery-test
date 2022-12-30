package ru.kggm.domain.usecases.files

import ru.kggm.domain.entities.File
import ru.kggm.domain.repositories.*
import ru.kggm.domain.usecases.folders.UpdateFolderFileCount
import ru.kggm.domain.utility.FlowExtensions.Companion.takeLast
import java.net.URI
import javax.inject.Inject

class RemoveFiles @Inject constructor(
    private val getFiles: GetFiles,

    private val fileRepository: FileRepository,
    private val updateFolderFileCount: UpdateFolderFileCount
) {
    internal suspend operator fun invoke(vararg files: File) {
        fileRepository.remove(files.toList())
        updateFolderFileCount(*files
            .map { it.folderUri }
            .distinct()
            .toTypedArray()
        )
    }

    suspend operator fun invoke(vararg fileUris: URI) {
        getFiles().takeLast()
            .filter { it.uri in fileUris }
            .also {
                invoke(*it.toTypedArray())
            }
    }
}