package ru.kggm.presentation.ui.folders

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.kggm.presentation.R
import ru.kggm.presentation.databinding.FragmentFoldersBinding
import ru.kggm.presentation.entities.FolderEntity
import ru.kggm.presentation.ui.files.FilesViewModel
import ru.kggm.presentation.ui.preferences.UserPreferencesViewModel
import ru.kggm.presentation.utility.UIElementExtensions.Companion.inLandscape

@AndroidEntryPoint
class FoldersFragment : Fragment() {
    private val viewModel: FoldersViewModel by activityViewModels()
    private val userPreferencesViewModel: UserPreferencesViewModel by activityViewModels()

    private var _binding: FragmentFoldersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoldersBinding.inflate(inflater, container, false)
        binding.toolbar.inflateMenu(R.menu.menu_folders)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        binding.fabAddFolder.setOnClickListener { fabAddFolderOnClick() }
        binding.fabDeleteFolder.setOnClickListener { fabDeleteFolderOnClick() }
        binding.toolbar.setOnMenuItemClickListener { onMenuItemClick(it) }
    }

    private lateinit var layoutManager: GridLayoutManager
    private fun initializeRecyclerView() {
        val adapter = FoldersRecyclerAdapter()
        binding.folders.adapter = adapter
        layoutManager = GridLayoutManager(requireContext(), 1)
        binding.folders.layoutManager = layoutManager
        folderSelectionTracker = SelectionTracker.Builder(
            "selection",
            binding.folders,
            FoldersRecyclerAdapter.FolderKeyProvider(adapter),
            FoldersRecyclerAdapter.FolderDetailsLookup(binding.folders),
            StorageStrategy.createParcelableStorage(Uri::class.java)
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
        adapter.selectionTracker = folderSelectionTracker

        viewModel.folders.observe(viewLifecycleOwner) { folders ->
            folders?.let { adapter.submitList(it) }
        }
        adapter.setOnFolderClickListener(FoldersRecyclerAdapter.OnClickListener { onFolderClick(it) })
        binding.swipeRefreshLayout.setOnRefreshListener { onRefresh() }
        folderSelectionTracker.addObserver(selectionObserver)

        userPreferencesViewModel.getFolderColumnCount(inLandscape).observe(viewLifecycleOwner) { onColumnsChanged(it) }
    }

    lateinit var folderSelectionTracker: SelectionTracker<Uri>
    private val selectionObserver = object: SelectionTracker.SelectionObserver<Uri>() {
        override fun onSelectionChanged() {
            super.onSelectionChanged()
            binding.fabDeleteFolder.visibility =
                if (folderSelectionTracker.selection.isEmpty)
                    View.INVISIBLE
                else View.VISIBLE
        }
    }
    private fun onFolderClick(folder: FolderEntity) {
        findNavController().navigate(
            R.id.action_foldersFragment_to_filesFragment,
            bundleOf(FilesViewModel.ARG_OPEN_FOLDER_URI to folder.uri)
        )
    }

    private val addFolderActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                DocumentFile.fromTreeUri(requireContext(), uri)?.let {
                    viewModel.addFolder(it.uri)
                }
            }
        }
    }
    private fun fabAddFolderOnClick() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        addFolderActivityLauncher.launch(intent)
    }
    private fun fabDeleteFolderOnClick() {
        viewModel.removeFolders(folderSelectionTracker.selection.toList())
    }

    private fun onRefresh() {
        viewModel.rescanFolders()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun onColumnsChanged(count: Int) {
        layoutManager.spanCount = count
    }

    private fun onMenuItemClick(menuItem: MenuItem) : Boolean {
        return when (menuItem.itemId) {
            R.id.menuItemMoreColumns -> {
                userPreferencesViewModel.changeFolderColumnCount(1, inLandscape)
                false
            }
            R.id.menuItemLessColumns -> {
                userPreferencesViewModel.changeFolderColumnCount(-1, inLandscape)
                false
            }
            R.id.menuItemSettings -> {
                findNavController().navigate(R.id.action_foldersFragment_to_userPreferencesFragment)
                true
            }
            else -> false
        }
    }
}