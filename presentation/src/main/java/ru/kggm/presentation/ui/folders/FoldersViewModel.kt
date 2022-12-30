package ru.kggm.presentation.ui.folders

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kggm.domain.usecases.files.AddFiles
import ru.kggm.domain.usecases.files.SetFilesForFolder
import ru.kggm.domain.usecases.folders.AddFolders
import ru.kggm.domain.usecases.folders.GetFolders
import ru.kggm.domain.usecases.folders.RemoveFolders
import ru.kggm.presentation.entities.FolderEntity.Companion.toEntity
import ru.kggm.presentation.utility.UriConversions.Companion.toURI
import ru.kggm.presentation.utility.UriExtensions.Companion.getChildren
import ru.kggm.presentation.utility.ViewModelExtensions.Companion.launchInViewModelScopeAsync
import javax.inject.Inject

@HiltViewModel
class FoldersViewModel @Inject constructor (
    private val getFolders: GetFolders,
    private val addFolders: AddFolders,
    private val removeFolders: RemoveFolders,
    private val setFilesForFolder: SetFilesForFolder,

    private val addFiles: AddFiles,

    private val contentResolver: ContentResolver
) : ViewModel() {
    val folders = getFolders()
        .asLiveData()
        .map { folders ->
            folders.map { it.toEntity() }
        }
        .distinctUntilChanged()

    fun addFolder(folderUri: Uri) = launchInViewModelScopeAsync {
        addFolders(folderUri.toURI())
        val fileUris = folderUri.getChildren(contentResolver).map { it.toURI() }
        addFiles(folderUri.toURI(), *fileUris.toTypedArray())
    }

    fun removeFolders(folderUris: List<Uri>) = launchInViewModelScopeAsync {
        folderUris
            .map { it.toURI() }
            .let {
                removeFolders(*it.toTypedArray())
            }
    }

    fun rescanFolders() = launchInViewModelScopeAsync {
        folders.value?.forEach { folder ->
            setFilesForFolder(
                folder.uri.toURI(),
                folder.uri.getChildren(contentResolver).map { it.toURI() }
            )
        }
    }
}