package ru.kggm.imagegallerytest.viewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.kggm.imagegallerytest.data.entities.Folder
import ru.kggm.imagegallerytest.data.entities.FolderWithFiles
import ru.kggm.imagegallerytest.data.repositories.FileRepository
import ru.kggm.imagegallerytest.data.repositories.FolderRepository
import ru.kggm.imagegallerytest.utility.FileSystemUtilities.Companion.exists
import ru.kggm.imagegallerytest.utility.FileSystemUtilities.Companion.getChildren
import java.util.*

class FolderViewModel(
    private val folderRepository: FolderRepository,
    private val fileRepository: FileRepository
)
    : ViewModel() {
    val allFolders: LiveData<List<Folder>> = folderRepository.allFolders.distinctUntilChanged().asLiveData()
    val allFoldersWithFiles: LiveData<List<FolderWithFiles>> = folderRepository.allFoldersWithFiles.distinctUntilChanged().asLiveData()
    var openedFolder: Folder? = null

    fun insert(folder: Folder, context: Context) = viewModelScope.launch {
        if (folderRepository.insert(folder) != -1L) {
            scanFolders(context)
        }
    }

    fun delete(folders: List<Folder>) = viewModelScope.launch {
        folderRepository.delete(folders)
    }
    fun deleteByUri(folderUris: List<Uri>) = viewModelScope.launch {
        folderUris.map { uri ->
            allFolders.value!!.first { folder -> folder.uri == uri }
        }.also { folderRepository.delete(it) }
    }

    fun scanFolders(context: Context) = viewModelScope.launch {
        folderRepository.allFoldersWithFiles.first { true }.forEach { folderWithFiles ->
            val folder = folderWithFiles.folder
            val oldFiles = folderWithFiles.files

            if (!folder.exists(context)) {
                folderRepository.delete(folder)
                return@forEach
            }
            val imageNameRegex = Regex(".*\\.(jpg|jpeg|png)$") // User Preference

            launch(Dispatchers.Default) {
                val currentFiles = folder.getChildren(context, imageNameRegex)
                val existingFiles = currentFiles
                    .filter { oldFiles.any { file -> file.uri == it.uri} }
                val newFiles = currentFiles
                    .filter { !oldFiles.any { file -> file.uri == it.uri} }
                val missingFiles = oldFiles
                    .filter { !currentFiles.any { file -> file.uri == it.uri} }
                //Log.println(Log.INFO, "", "getFilesData in ${measureTimeMillis { getFiles() }}ms")

                fileRepository.update(existingFiles)
                fileRepository.insert(newFiles)
                fileRepository.delete(missingFiles)
                folderRepository.update(folder.copy(fileCount = folderRepository.countFiles(folder)))
            }
        }
    }
}

class FolderViewModelFactory(
    private val folderRepository: FolderRepository,
    private val fileRepository: FileRepository
)
    : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(FolderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FolderViewModel(folderRepository, fileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}