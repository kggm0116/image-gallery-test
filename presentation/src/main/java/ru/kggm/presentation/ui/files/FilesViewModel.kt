package ru.kggm.presentation.ui.files

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kggm.domain.usecases.files.GetFilesForFolder
import ru.kggm.domain.usecases.files.SetFilesForFolder
import ru.kggm.presentation.entities.FileEntity.Companion.toEntity
import ru.kggm.presentation.utility.UriConversions.Companion.toURI
import ru.kggm.presentation.utility.UriExtensions.Companion.getChildren
import ru.kggm.presentation.utility.ViewModelExtensions.Companion.launchInViewModelScopeAsync
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor (
    private val getFilesForFolder: GetFilesForFolder,
    private val setFilesForFolder: SetFilesForFolder,

    private val savedStateHandle: SavedStateHandle,
    private val contentResolver: ContentResolver
) : ViewModel() {
    companion object {
        const val ARG_OPEN_FOLDER_URI = "Open_folder_uri"
        const val ARG_OPEN_FILE_URI = "Open_file_uri"
        const val ARG_CLOSE_FILE_URI = "Close_file_uri"
    }
    val openFolderUri = savedStateHandle.get<Uri>(ARG_OPEN_FOLDER_URI)
        ?: throw Error("Failed to retrieve open folder argument")
    val openFileUri = MutableLiveData(savedStateHandle.get<Uri>(ARG_OPEN_FILE_URI))
    val closeFileUri = MutableLiveData(savedStateHandle.get<Uri>(ARG_CLOSE_FILE_URI))

    val files = getFilesForFolder(openFolderUri.toURI())
        .asLiveData()
        .map { files ->
            files.map { it.toEntity(openFolderUri) }
        }

    fun rescanOpenFolder() = launchInViewModelScopeAsync {
        setFilesForFolder(
            openFolderUri.toURI(),
            openFolderUri.getChildren(contentResolver)
                .map { it.toURI() }
        )
    }
}