package ru.kggm.presentation.entities

import android.net.Uri
import ru.kggm.domain.entities.Folder
import ru.kggm.presentation.utility.UriConversions.Companion.toUri
import ru.kggm.presentation.utility.UriExtensions.Companion.getName

data class FolderEntity(
    val name: String,
    val uri: Uri,
    var fileCount: Int = 0
) {
    companion object {
        fun Folder.toEntity() = FolderEntity(
            uri = uri.toUri(),
            name = name,
            fileCount = fileCount
        )
    }
}