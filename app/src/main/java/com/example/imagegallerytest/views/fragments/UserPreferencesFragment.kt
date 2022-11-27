package com.example.imagegallerytest.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.example.imagegallerytest.ImageGalleryApplication
import com.example.imagegallerytest.R
import com.example.imagegallerytest.viewModels.UserPreferencesViewModel
import com.example.imagegallerytest.viewModels.UserPreferencesViewModelFactory
import kotlinx.coroutines.launch

class UserPreferencesFragment : PreferenceFragmentCompat() {

    private val application get() = requireActivity().application as ImageGalleryApplication
    private val userPreferencesViewModel: UserPreferencesViewModel by activityViewModels {
        UserPreferencesViewModelFactory(
            application.userPreferencesRepository
        )
    }

    private lateinit var foldersGridColumnCountLandscapePreference: SeekBarPreference
    private lateinit var foldersGridColumnCountPortraitPreference: SeekBarPreference
    private lateinit var filesGridColumnCountLandscapePreference: SeekBarPreference
    private lateinit var filesGridColumnCountPortraitPreference: SeekBarPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.user_preferences, rootKey)

        foldersGridColumnCountLandscapePreference = findPreference<SeekBarPreference>(
            getString(R.string.preference_key_folders_grid_columns_landscape)
        )!!.apply {
            setOnPreferenceChangeListener { _, newValue ->
                lifecycleScope.launch { userPreferencesViewModel.setFoldersGridColumnsLandscape(newValue as Int) }
                true
            }
        }

        foldersGridColumnCountPortraitPreference = findPreference<SeekBarPreference>(
            getString(R.string.preference_key_folders_grid_columns_portrait)
        )!!.apply {
            setOnPreferenceChangeListener { _, newValue ->
                lifecycleScope.launch { userPreferencesViewModel.setFoldersGridColumnsPortrait(newValue as Int) }
                true
            }
        }

        filesGridColumnCountLandscapePreference = findPreference<SeekBarPreference>(
            getString(R.string.preference_key_files_grid_columns_landscape)
        )!!.apply {
            setOnPreferenceChangeListener { _, newValue ->
                lifecycleScope.launch { userPreferencesViewModel.setFilesGridColumnsLandscape(newValue as Int) }
                true
            }
        }

        filesGridColumnCountPortraitPreference = findPreference<SeekBarPreference>(
            getString(R.string.preference_key_files_grid_columns_portrait)
        )!!.apply {
            setOnPreferenceChangeListener { _, newValue ->
                lifecycleScope.launch { userPreferencesViewModel.setFilesGridColumnsPortrait(newValue as Int) }
                true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userPreferencesViewModel.getLatest.observe(viewLifecycleOwner) {
            foldersGridColumnCountLandscapePreference.value = it.foldersGridColumnsLandscape
            foldersGridColumnCountPortraitPreference.value = it.foldersGridColumnsPortrait
            filesGridColumnCountLandscapePreference.value = it.filesGridColumnsLandscape
            filesGridColumnCountPortraitPreference.value = it.filesGridColumnsPortrait
        }
        super.onViewCreated(view, savedInstanceState)
    }
}