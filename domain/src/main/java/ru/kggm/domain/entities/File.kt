package ru.kggm.domain.entities

import java.net.URI

data class File(
    val uri: URI,
    val folderUri: URI,
    val name: String,
    val extension: String
) {
    companion object {
        val REGEX_SUPPORTED_FILE_EXTENSION = Regex("\\.[a-zA-Z0-9]+")
    }
    init {
        require(REGEX_SUPPORTED_FILE_EXTENSION.matches(extension)) {
            "File extension must match regex $REGEX_SUPPORTED_FILE_EXTENSION"
        }
    }
}
