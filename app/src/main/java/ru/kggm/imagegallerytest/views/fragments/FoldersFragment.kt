package ru.kggm.imagegallerytest.views.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagegallerytest.*
import com.example.imagegallerytest.databinding.FragmentFoldersBinding
import ru.kggm.imagegallerytest.data.entities.Folder
import ru.kggm.imagegallerytest.utility.FileSystemUtilities.Companion.toFolder
import ru.kggm.imagegallerytest.viewModels.FolderViewModel
import ru.kggm.imagegallerytest.viewModels.FolderViewModelFactory
import ru.kggm.imagegallerytest.viewModels.UserPreferencesViewModel
import ru.kggm.imagegallerytest.viewModels.UserPreferencesViewModelFactory
import ru.kggm.imagegallerytest.views.adapters.FoldersRecyclerAdapter
import java.util.*

class FoldersFragment : Fragment() {
    private var _binding: FragmentFoldersBinding? = null
    private val binding get() = _binding!!
    private val application get() = requireActivity().application as ru.kggm.imagegallerytest.ImageGalleryApplication
    private val viewModel: FolderViewModel by activityViewModels {
        FolderViewModelFactory(
            application.folderRepository,
            application.fileRepository,)
    }
    private val userPreferencesViewModel: UserPreferencesViewModel by activityViewModels {
        UserPreferencesViewModelFactory(
            application.userPreferencesRepository
        )
    }
    lateinit var selectionTracker: SelectionTracker<Uri>

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

        val adapter = FoldersRecyclerAdapter()
        binding.folders.adapter = adapter
        selectionTracker = SelectionTracker.Builder(
            "selection",
            binding.folders,
            FoldersRecyclerAdapter.FolderKeyProvider(adapter),
            FoldersRecyclerAdapter.FolderDetailsLookup(binding.folders),
            StorageStrategy.createParcelableStorage(Uri::class.java)
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
        viewModel.allFolders.observe(viewLifecycleOwner) { folders -> folders?.let { adapter.submitList(it) } }
        adapter.setFolderOnClickListener(FoldersRecyclerAdapter.OnClickListener { folderOnClick(it) })
        binding.swipeRefreshLayout.setOnRefreshListener { onRefresh() }
        selectionTracker.addObserver(selectionObserver)
        adapter.selectionTracker = selectionTracker

        binding.fabAddFolder.setOnClickListener { fabAddFolderOnClick(it) }
        binding.fabDeleteFolder.setOnClickListener { fabDeleteFolderOnClick(it) }
        binding.toolbar.setOnMenuItemClickListener { menuItemOnClick(it) }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            userPreferencesViewModel.foldersGridColumnsLandscape.observe(viewLifecycleOwner) { onLandscapeGridColumnCountChanged() }
            userPreferencesViewModel.changeFoldersGridColumnsLandscape(0)
        }
        else {
            userPreferencesViewModel.foldersGridColumnsPortrait.observe(viewLifecycleOwner) { onPortraitGridColumnCountChanged() }
            userPreferencesViewModel.changeFoldersGridColumnsPortrait(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val selectionObserver = object: SelectionObserver<Uri>() {
        override fun onSelectionChanged() {
            super.onSelectionChanged()
            binding.fabDeleteFolder.visibility =
                if (selectionTracker.selection.isEmpty)
                    View.INVISIBLE
                else View.VISIBLE
        }
    }

    @SuppressLint("Range")
    val addFolderActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.insert(uri.toFolder(requireContext()), requireContext())
            }
        }
    }

    private fun folderOnClick(folder: Folder) {
        viewModel.openedFolder = folder
        findNavController().navigate(R.id.action_foldersFragment_to_navigationGraphFiles)
    }

    private fun fabAddFolderOnClick(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        addFolderActivityLauncher.launch(intent)
    }

    private fun fabDeleteFolderOnClick(view: View) {
        viewModel.deleteByUri(selectionTracker.selection.toList())
    }

    private fun onRefresh() {
        viewModel.scanFolders(requireContext())
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun onPortraitGridColumnCountChanged() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (binding.folders.layoutManager as GridLayoutManager)
                .spanCount = userPreferencesViewModel.foldersGridColumnsPortrait.value!!
    }

    private fun onLandscapeGridColumnCountChanged() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            (binding.folders.layoutManager as GridLayoutManager)
                .spanCount = userPreferencesViewModel.foldersGridColumnsLandscape.value!!
    }

    private fun menuItemOnClick(menuItem: MenuItem) : Boolean {
        return when (menuItem.itemId) {
            R.id.menuItemMoreColumns -> {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    userPreferencesViewModel.changeFoldersGridColumnsLandscape(1)
                else
                    userPreferencesViewModel.changeFoldersGridColumnsPortrait(1)
                true
            }
            R.id.menuItemLessColumns -> {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    userPreferencesViewModel.changeFoldersGridColumnsLandscape(-1)
                else
                    userPreferencesViewModel.changeFoldersGridColumnsPortrait(-1)
                true
            }
            R.id.menuItemSettings -> {
                findNavController().navigate(R.id.action_foldersFragment_to_userPreferencesFragment)
                true
            }
            else -> false
        }
    }
}