package ru.kggm.domain.usecases.files

import kotlinx.coroutines.flow.map
import java.net.URI
import javax.inject.Inject

class GetFilesForFolder @Inject constructor (
    private val getFiles: GetFiles
) {
    operator fun invoke(folderUri: URI) =
        getFiles()
            .map { files ->
                files.filter { it.folderUri == folderUri }
            }
}