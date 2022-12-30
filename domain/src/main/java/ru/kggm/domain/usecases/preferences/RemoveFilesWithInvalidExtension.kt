package ru.kggm.domain.usecases.preferences

import ru.kggm.domain.usecases.files.GetFiles
import ru.kggm.domain.usecases.files.RemoveFiles
import ru.kggm.domain.utility.FlowExtensions.Companion.takeLast
import javax.inject.Inject

class RemoveFilesWithInvalidExtension @Inject constructor(
    private val getFiles: GetFiles,
    private val removeFiles: RemoveFiles
) {
    suspend operator fun invoke(validExtensions: List<String>) {
        getFiles().takeLast()
            .filter { it.extension !in validExtensions }
            .let {
                removeFiles(*it.toTypedArray())
            }
    }
}