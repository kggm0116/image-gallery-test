package ru.kggm.domain.usecases.folders

import ru.kggm.domain.repositories.FolderRepository
import javax.inject.Inject

class GetFolders @Inject constructor (
    private val folderRepository: FolderRepository
) {
    operator fun invoke() = folderRepository.get()
}