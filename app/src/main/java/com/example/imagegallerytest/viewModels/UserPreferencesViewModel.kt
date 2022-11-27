package com.example.imagegallerytest.viewModels

import UserPreferencesRepository
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class UserPreferencesViewModel(
    private val prefsRepository: UserPreferencesRepository
    ) : ViewModel() {

    val getLatest get() = prefsRepository.userPreferences.asLiveData()

    val foldersGridColumnsLandscape = prefsRepository.userPreferences.asLiveData()
        .map { it.foldersGridColumnsLandscape }
    val foldersGridColumnsPortrait = prefsRepository.userPreferences.asLiveData()
        .map { it.foldersGridColumnsPortrait }
    val filesGridColumnsLandscape = prefsRepository.userPreferences.asLiveData()
        .map { it.filesGridColumnsLandscape }
    val filesGridColumnsPortrait = prefsRepository.userPreferences.asLiveData()
        .map { it.filesGridColumnsPortrait }

    fun setFoldersGridColumnsLandscape(value: Int) = viewModelScope.launch {
        prefsRepository.updateFoldersGridColumnsLandscape(value)
    }
    fun setFoldersGridColumnsPortrait(value: Int) = viewModelScope.launch {
        prefsRepository.updateFoldersGridColumnsPortrait(value)
    }
    fun changeFoldersGridColumnsLandscape(change: Int) = viewModelScope.launch {
        foldersGridColumnsLandscape.value?.let {
            prefsRepository.updateFoldersGridColumnsLandscape(it + change  )
        }
    }
    fun changeFoldersGridColumnsPortrait(change: Int) = viewModelScope.launch {
        foldersGridColumnsPortrait.value?.let {
            prefsRepository.updateFoldersGridColumnsPortrait(it + change  )
        }
    }
    fun setFilesGridColumnsLandscape(value: Int) = viewModelScope.launch {
        prefsRepository.updateFilesGridColumnsLandscape(value)
    }
    fun setFilesGridColumnsPortrait(value: Int) = viewModelScope.launch {
        prefsRepository.updateFilesGridColumnsPortrait(value)
    }
    fun changeFilesGridColumnsLandscape(change: Int) = viewModelScope.launch {
        filesGridColumnsLandscape.value?.let {
            prefsRepository.updateFilesGridColumnsLandscape(it + change  )
        }
    }
    fun changeFilesGridColumnsPortrait(change: Int) = viewModelScope.launch {
        filesGridColumnsPortrait.value?.let {
            prefsRepository.updateFilesGridColumnsPortrait(it + change  )
        }
    }
}

class UserPreferencesViewModelFactory(
    private val prefsRepository: UserPreferencesRepository
)
    : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(UserPreferencesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserPreferencesViewModel(prefsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}