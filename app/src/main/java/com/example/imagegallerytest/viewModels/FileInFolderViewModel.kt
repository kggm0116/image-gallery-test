package com.example.imagegallerytest.viewModels

import androidx.lifecycle.*
import com.example.imagegallerytest.data.entities.File
import com.example.imagegallerytest.data.entities.Folder
import com.example.imagegallerytest.data.repositories.FolderRepository
import kotlinx.coroutines.*

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