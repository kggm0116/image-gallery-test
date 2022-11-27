package com.example.imagegallerytest.views.fragments

import android.content.res.Configuration
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
import com.example.imagegallerytest.ImageGalleryApplication
import com.example.imagegallerytest.R
import com.example.imagegallerytest.data.entities.File
import com.example.imagegallerytest.databinding.FragmentFilesBinding
import com.example.imagegallerytest.viewModels.*
import com.example.imagegallerytest.views.adapters.FilesRecyclerAdapter

class FilesFragment : Fragment() {

    private var _binding: FragmentFilesBinding? = null
    private val binding get() = _binding!!
    private val application get() = requireActivity().application as ImageGalleryApplication
    private val folderViewModel: FolderViewModel by activityViewModels {
        FolderViewModelFactory(
            application.folderRepository,
            application.fileRepository,)
    }
    private val userPreferencesViewModel: UserPreferencesViewModel by activityViewModels {
        UserPreferencesViewModelFactory(
            application.userPreferencesRepository
        )
    }

    private val fileViewModel: FileInFolderViewModel by navGraphViewModels(R.id.navigation_graph_files) {
        FileInFolderViewModelFactory(
            folderViewModel.openedFolder
                ?: throw Error("OpenedFolder property in FileInFolderViewModel must be set before creating FilesFragment"),
            application.folderRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilesBinding.inflate(inflater, container, false)
        binding.toolbar.inflateMenu(R.menu.menu_files)
        return binding.root
    }

    private lateinit var filesAdapter: FilesRecyclerAdapter
    private lateinit var layoutManager: GridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.folder = folderViewModel.openedFolder
        filesAdapter = FilesRecyclerAdapter()
        binding.files.adapter = filesAdapter
        layoutManager = GridLayoutManager(requireContext(), getColumnCount() ?: 3)
        binding.files.layoutManager = layoutManager
        fileViewModel.files.observe(viewLifecycleOwner) { onFilesChanged(it) }
        fileViewModel.closedFile.observe(viewLifecycleOwner) { onClosedFileChanged(it) }

        binding.swipeRefreshLayout.setOnRefreshListener { onRefresh() }
        filesAdapter.setFileOnClickListener(FilesRecyclerAdapter.OnClickListener { fileOnClick(it) })
        binding.toolbar.setOnMenuItemClickListener { menuItemOnClick(it) }
        binding.files.setItemViewCacheSize(0)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            userPreferencesViewModel.filesGridColumnsLandscape.observe(viewLifecycleOwner) { onLandscapeGridColumnCountChanged() }
            userPreferencesViewModel.changeFilesGridColumnsLandscape(0)
        }
        else {
            userPreferencesViewModel.filesGridColumnsPortrait.observe(viewLifecycleOwner) { onPortraitGridColumnCountChanged() }
            userPreferencesViewModel.changeFilesGridColumnsPortrait(0)
        }
    }

    private fun onFilesChanged(files: List<File>) {
        filesAdapter.submitList(files)
    }

    private fun fileOnClick(file: File) {
        fileViewModel.openedFile = file
        findNavController().navigate(R.id.action_browseFilesFragment_to_imageFragment)
    }

    private fun onRefresh() {
        folderViewModel.scanFolders(requireContext())
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun onPortraitGridColumnCountChanged() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (binding.files.layoutManager as GridLayoutManager)
                .spanCount = userPreferencesViewModel.filesGridColumnsPortrait.value!!
    }

    private fun onLandscapeGridColumnCountChanged() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            (binding.files.layoutManager as GridLayoutManager)
                .spanCount = userPreferencesViewModel.filesGridColumnsLandscape.value!!
    }

    private fun onClosedFileChanged(file: File?) {
        if (file != null)
            binding.files.scrollToPosition(fileViewModel.files.value?.indexOf(file) ?: 0)
    }

    private fun menuItemOnClick(menuItem: MenuItem) : Boolean {
        return when (menuItem.itemId) {
            R.id.menuItemMoreColumns -> {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    userPreferencesViewModel.changeFilesGridColumnsLandscape(1)
                else
                    userPreferencesViewModel.changeFilesGridColumnsPortrait(1)
                true
            }
            R.id.menuItemLessColumns -> {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    userPreferencesViewModel.changeFilesGridColumnsLandscape(-1)
                else
                    userPreferencesViewModel.changeFilesGridColumnsPortrait(-1)
                true
            }
            R.id.menuItemSettings -> {
                findNavController().navigate(R.id.action_filesFragment_to_userPreferencesFragment)
                true
            }
            else -> false
        }
    }

    private fun getColumnCount() =
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            userPreferencesViewModel.filesGridColumnsLandscape.value
        else
            userPreferencesViewModel.filesGridColumnsPortrait.value
}