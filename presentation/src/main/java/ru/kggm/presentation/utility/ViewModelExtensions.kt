package ru.kggm.presentation.utility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelExtensions {
    companion object {
        fun ViewModel.launchInViewModelScopeAsync(block: suspend CoroutineScope.() -> Unit) {
            viewModelScope.launch {
                launch(Dispatchers.Default, block = block)
            }
        }
    }
}