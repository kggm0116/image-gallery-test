package ru.kggm.presentation.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kggm.domain.usecases.preferences.GetUserPreferences
import ru.kggm.domain.usecases.preferences.UpdateUserPreferences
import ru.kggm.presentation.utility.ViewModelExtensions.Companion.launchInViewModelScopeAsync
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor (
    private val getUserPreferences: GetUserPreferences,
    private val updateUserPreferences: UpdateUserPreferences
    ) : ViewModel() {

    val preferences = getUserPreferences().asLiveData()

    val folderColumnsCountLandscape = preferences.map { it.folderColumnCountLandscape }
    val folderColumnsCountPortrait = preferences.map { it.folderColumnCountPortrait }
    val fileColumnsCountLandscape = preferences.map { it.fileColumnCountLandscape }
    val fileColumnsCountPortrait = preferences.map { it.fileColumnCountPortrait }
    val supportedFileExtensions = preferences.map { it.supportedFileExtensions }

    fun setFolderColumnsCountLandscape(value: Int) = launchInViewModelScopeAsync {
        updateUserPreferences.setFolderColumnCountLandscape(value)
    }
    fun changeFolderColumnsCountLandscape(change: Int) = launchInViewModelScopeAsync {
        updateUserPreferences.setFolderColumnCountLandscape(folderColumnsCountLandscape.value!! + change)
    }

    fun setFolderColumnsCountPortrait(value: Int) = launchInViewModelScopeAsync {
        updateUserPreferences.setFolderColumnCountPortrait(value)
    }
    fun changeFolderColumnsCountPortrait(change: Int) = launchInViewModelScopeAsync {
        updateUserPreferences.setFolderColumnCountPortrait(folderColumnsCountPortrait.value!! + change)
    }

    fun setFileColumnsCountLandscape(value: Int) = launchInViewModelScopeAsync {
        updateUserPreferences.setFileColumnCountLandscape(value)
    }
    fun changeFileColumnsCountLandscape(change: Int) = launchInViewModelScopeAsync {
        updateUserPreferences.setFileColumnCountLandscape(fileColumnsCountLandscape.value!! + change)
    }

    fun setFileColumnsCountPortrait(value: Int) = launchInViewModelScopeAsync {
        updateUserPreferences.setFileColumnCountPortrait(value)
    }
    fun changeFileColumnsCountPortrait(change: Int) = launchInViewModelScopeAsync {
        updateUserPreferences.setFileColumnCountPortrait(fileColumnsCountPortrait.value!! + change)
    }

    fun setSupportedFileExtensions(value: List<String>) = launchInViewModelScopeAsync {
        updateUserPreferences.setSupportedFileExtensions(value)
    }

    fun getFileColumnCount(landscape: Boolean) =
        if (landscape) fileColumnsCountLandscape
        else fileColumnsCountPortrait
    fun setFileColumnsCount(value: Int, landscape: Boolean) =
        if (landscape) setFileColumnsCountLandscape(value)
        else setFileColumnsCountPortrait(value)
    fun changeFileColumnCount(change: Int, landscape: Boolean) =
        if (landscape) changeFileColumnsCountLandscape(change)
        else changeFileColumnsCountPortrait(change)

    fun getFolderColumnCount(landscape: Boolean) =
        if (landscape) folderColumnsCountLandscape
        else folderColumnsCountPortrait
    fun setFolderColumnCount(value: Int, landscape: Boolean) =
        if (landscape) setFolderColumnsCountLandscape(value)
        else setFolderColumnsCountPortrait(value)
    fun changeFolderColumnCount(change: Int, landscape: Boolean) =
        if (landscape) changeFolderColumnsCountLandscape(change)
        else changeFolderColumnsCountPortrait(change)
}