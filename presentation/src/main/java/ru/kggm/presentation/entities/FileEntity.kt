package ru.kggm.presentation.entities

import android.net.Uri
import ru.kggm.domain.entities.File
import ru.kggm.presentation.utility.UriConversions.Companion.toUri
import ru.kggm.presentation.utility.UriExtensions.Companion.getName

data class FileEntity(
    val name: String,
    val extension: String,
    val uri: Uri,
    val folderUri: Uri
) {
    companion object {
        fun File.toEntity(folderUri: Uri) = FileEntity(
            uri = uri.toUri(),
            folderUri = folderUri,
            name = name,
            extension = extension)
    }
}
