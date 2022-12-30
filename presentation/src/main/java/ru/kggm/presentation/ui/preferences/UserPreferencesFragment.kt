package ru.kggm.presentation.ui.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import dagger.hilt.android.AndroidEntryPoint
import ru.kggm.domain.entities.UserPreferences
import ru.kggm.presentation.R

@AndroidEntryPoint
class UserPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: UserPreferencesViewModel by activityViewModels()

    private lateinit var folderColumnCountLandscapePreference: SeekBarPreference
    private lateinit var folderColumnCountPortraitPreference: SeekBarPreference
    private lateinit var fileColumnCountLandscapePreference: SeekBarPreference
    private lateinit var fileColumnCountPortraitPreference: SeekBarPreference
    private lateinit var supportedFileExtensionsPreference: EditTextPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.folderColumnsCountLandscape.observe(viewLifecycleOwner) {
            folderColumnCountLandscapePreference.value = it
        }
        viewModel.folderColumnsCountPortrait.observe(viewLifecycleOwner) {
            folderColumnCountPortraitPreference.value = it
        }
        viewModel.fileColumnsCountLandscape.observe(viewLifecycleOwner) {
            fileColumnCountLandscapePreference.value = it
        }
        viewModel.fileColumnsCountPortrait.observe(viewLifecycleOwner) {
            fileColumnCountPortraitPreference.value = it
        }
        viewModel.supportedFileExtensions.observe(viewLifecycleOwner) {
            supportedFileExtensionsPreference.text = it.joinToString(" ")
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_user_preferences, rootKey)

        folderColumnCountLandscapePreference = findPreference<SeekBarPreference>(
            getString(R.string.preference_key_folder_column_count_landscape)
        )!!.apply {
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.setFolderColumnsCountLandscape(newValue as Int)
                true
            }
            min = UserPreferences.MIN_FOLDER_COLUMN_COUNT_LANDSCAPE
            max = UserPreferences.MAX_FOLDER_COLUMN_COUNT_LANDSCAPE
            setDefaultValue(UserPreferences.DEFAULT_FOLDER_COLUMN_COUNT_LANDSCAPE)
        }

        folderColumnCountPortraitPreference = findPreference<SeekBarPreference>(
            getString(R.string.preference_key_folder_column_count_portrait)
        )!!.apply {
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.setFolderColumnsCountPortrait(newValue as Int)
                true
            }
            min = UserPreferences.MIN_FOLDER_COLUMN_COUNT_PORTRAIT
            max = UserPreferences.MAX_FOLDER_COLUMN_COUNT_PORTRAIT
            setDefaultValue(UserPreferences.DEFAULT_FOLDER_COLUMN_COUNT_PORTRAIT)
        }

        fileColumnCountLandscapePreference = findPreference<SeekBarPreference>(
            getString(R.string.preference_key_file_column_count_landscape)
        )!!.apply {
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.setFileColumnsCountLandscape(newValue as Int)
                true
            }
            min = UserPreferences.MIN_FILE_COLUMN_COUNT_LANDSCAPE
            max = UserPreferences.MAX_FILE_COLUMN_COUNT_LANDSCAPE
            setDefaultValue(UserPreferences.DEFAULT_FILE_COLUMN_COUNT_LANDSCAPE)
        }

        fileColumnCountPortraitPreference = findPreference<SeekBarPreference>(
            getString(R.string.preference_key_file_column_count_portrait)
        )!!.apply {
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.setFileColumnsCountPortrait(newValue as Int)
                true
            }
            min = UserPreferences.MIN_FILE_COLUMN_COUNT_PORTRAIT
            max = UserPreferences.MAX_FILE_COLUMN_COUNT_PORTRAIT
            setDefaultValue(UserPreferences.DEFAULT_FILE_COLUMN_COUNT_PORTRAIT)
        }

        supportedFileExtensionsPreference = findPreference<EditTextPreference>(
            getString(R.string.preference_key_supported_file_extensions)
        )!!.apply {
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.setSupportedFileExtensions(
                    (newValue as String)
                        .split(' ')
                        .map { it.trim() }
                )
                this.text = viewModel.supportedFileExtensions.value!!.joinToString(" ")
                newValue == this.text
            }
            setDefaultValue(UserPreferences.DEFAULT_SUPPORTED_FILE_EXTENSIONS.joinToString(" "))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.preferences.observe(viewLifecycleOwner) {
            folderColumnCountLandscapePreference.value = it.folderColumnCountLandscape
            folderColumnCountPortraitPreference.value = it.folderColumnCountPortrait
            fileColumnCountLandscapePreference.value = it.fileColumnCountLandscape
            fileColumnCountPortraitPreference.value = it.fileColumnCountPortrait
            supportedFileExtensionsPreference.text = it.supportedFileExtensions.joinToString(" ")
            supportedFileExtensionsPreference.summary = it.supportedFileExtensions.joinToString(" ")
        }
        super.onViewCreated(view, savedInstanceState)
    }
}