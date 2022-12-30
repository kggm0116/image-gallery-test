package ru.kggm.domain.entities

import ru.kggm.domain.utility.UriExtensions.Companion.getName
import java.net.URI

data class Folder(
    val uri: URI,
    val name: String,
    val fileCount: Int = 0
) {
    init {
        require(fileCount >= 0) { "File count must be non-negative" }
    }

    companion object {
        fun fromUri(uri: URI) = Folder(uri, uri.getName())
    }
}
