package com.example.imagegallerytest.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.navGraphViewModels
import coil.ImageLoader
import coil.load
import com.example.imagegallerytest.ImageGalleryApplication
import com.example.imagegallerytest.R
import com.example.imagegallerytest.data.entities.File
import com.example.imagegallerytest.databinding.FragmentImageBinding
import com.example.imagegallerytest.viewModels.FileInFolderViewModel
import com.example.imagegallerytest.viewModels.FileInFolderViewModelFactory
import com.example.imagegallerytest.viewModels.FolderViewModel
import com.example.imagegallerytest.viewModels.FolderViewModelFactory
import kotlin.math.abs

class ImageFragment : Fragment() {
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    private val application get() = requireActivity().application as ImageGalleryApplication
    private val folderViewModel: FolderViewModel by activityViewModels {
        FolderViewModelFactory(
            application.folderRepository,
            application.fileRepository,)
    }
    private val fileViewModel: FileInFolderViewModel by navGraphViewModels(R.id.navigation_graph_files) {
        FileInFolderViewModelFactory(
            folderViewModel.openedFolder ?:
                throw Error("OpenedFolder property in FileInFolderViewModel must be set before creating ImageFragment"),
            application.folderRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var orderedFiles: List<File>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fileViewModel.files.value!!.let {
            val startingIndex = it.indexOf(fileViewModel.openedFile!!)
            orderedFiles = it.drop(startingIndex) + it.take(startingIndex)
        }

        binding.imagesCarousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int = orderedFiles.count()
            val imageLoader = ImageLoader.Builder(requireContext())
                .crossfade(false)
                .build()
            override fun populate(view: View, index: Int) {
                // Setting source natively hangs UI
                // Glide produces flicker
                // Coil works fine, but images can't load fast enough on quick swipes (might apply to Glide as well)
                // Custom image sources handler is hard to implement, and would it even help?
                (view as ImageView).load(getFile(index)?.uri, imageLoader)
            }
            override fun onNewItem(index: Int) { }
        }.apply {
            populate(binding.imageFarLeft, -2)
            populate(binding.imageLeft, -1)
            populate(binding.imageCenter, 0)
            populate(binding.imageRight, 1)
            populate(binding.imageFarRight, 2)
        })
    }

    private fun getFile(index: Int) =
        when {
            abs(index) >= orderedFiles.count() -> null
            index >= 0 -> orderedFiles[index]
            else -> orderedFiles[orderedFiles.count() + index]
        }

    override fun onDestroy() {
        fileViewModel.closedFile.value = getFile(binding.imagesCarousel.currentIndex)
        super.onDestroy()
    }
}