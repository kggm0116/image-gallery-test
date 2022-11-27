package ru.kggm.imagegallerytest.viewModels

import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.kggm.imagegallerytest.data.entities.File
import ru.kggm.imagegallerytest.data.entities.Folder
import ru.kggm.imagegallerytest.data.repositories.FolderRepository

class FileInFolderViewModel(
    private val folder: Folder,
    private val folderRepository: FolderRepository
)
    : ViewModel() {
    val files = folderRepository.allFoldersWithFiles.asLiveData()
        .map { folders ->
            folders.first { folderWithFiles -> folderWithFiles.folder == folder }
                .files.sortedBy { it.name }
        }

    var openedFile: File? = null
    var closedFile: MutableLiveData<File?> = MutableLiveData(null)
}

class FileInFolderViewModelFactory(
    private val folder: Folder,
    private val folderRepository: FolderRepository
)
    : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(FileInFolderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FileInFolderViewModel(folder, folderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}