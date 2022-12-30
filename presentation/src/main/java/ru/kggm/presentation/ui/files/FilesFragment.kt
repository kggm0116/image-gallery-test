package ru.kggm.presentation.ui.files

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.kggm.presentation.R
import ru.kggm.presentation.databinding.FragmentFilesBinding
import ru.kggm.presentation.entities.FileEntity
import ru.kggm.presentation.ui.preferences.UserPreferencesViewModel
import ru.kggm.presentation.utility.UIElementExtensions.Companion.inLandscape
import ru.kggm.presentation.utility.UriExtensions.Companion.getName

@AndroidEntryPoint
class FilesFragment : Fragment() {
    private val viewModel: FilesViewModel by navGraphViewModels(R.id.graph_files) { defaultViewModelProviderFactory }
    private val userPreferencesViewModel: UserPreferencesViewModel by activityViewModels()

    private var _binding: FragmentFilesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilesBinding.inflate(inflater, container, false)
        binding.toolbar.inflateMenu(R.menu.menu_files)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = viewModel.openFolderUri.getName()
        initializeRecyclerView()
        viewModel.closeFileUri.observe(viewLifecycleOwner) { onFocusFileChanged(it) }
        binding.toolbar.setOnMenuItemClickListener { onMenuItemClick(it) }
    }

    private lateinit var layoutManager: GridLayoutManager
    private fun initializeRecyclerView() {
        val adapter = FilesRecyclerAdapter()
        binding.files.adapter = adapter
        layoutManager = GridLayoutManager(requireContext(), 1)
        binding.files.layoutManager = layoutManager

        viewModel.files.observe(viewLifecycleOwner) { files -> files?.let { adapter.submitList(it) } }
        adapter.setOnFileClickListener(FilesRecyclerAdapter.OnClickListener { onFileClick(it) })
        binding.swipeRefreshLayout.setOnRefreshListener { onRefresh() }

        userPreferencesViewModel.getFileColumnCount(inLandscape)
            .observe(viewLifecycleOwner) { onColumnCountChanged(it) }
    }

    private fun onFileClick(file: FileEntity) {
        viewModel.openFileUri.value = file.uri
        findNavController().navigate(R.id.action_filesFragment_to_imageFragment)
    }

    private fun onRefresh() {
        viewModel.rescanOpenFolder()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun onFocusFileChanged(newUri: Uri?) {
        val pos = viewModel.files.value ?.indexOfFirst { it.uri == newUri } ?: 0
        binding.files.scrollToPosition(pos)
    }

    private fun onColumnCountChanged(count: Int) {
        layoutManager.spanCount = count
    }

    private fun onMenuItemClick(menuItem: MenuItem) : Boolean {
        return when (menuItem.itemId) {
            R.id.menuItemMoreColumns -> {
                userPreferencesViewModel.changeFileColumnCount(1, inLandscape)
                false
            }
            R.id.menuItemLessColumns -> {
                userPreferencesViewModel.changeFileColumnCount(-1, inLandscape)
                false
            }
            R.id.menuItemSettings -> {
                findNavController().navigate(R.id.action_filesFragment_to_userPreferencesFragment)
                true
            }
            else -> false
        }
    }
}