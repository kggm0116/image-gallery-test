package ru.kggm.presentation.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import coil.ImageLoader
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ru.kggm.presentation.R
import ru.kggm.presentation.databinding.FragmentGalleryBinding
import ru.kggm.presentation.entities.FileEntity
import ru.kggm.presentation.ui.files.FilesViewModel
import kotlin.math.abs

@AndroidEntryPoint
class GalleryFragment : Fragment() {
    private val viewModel: FilesViewModel by navGraphViewModels(R.id.graph_files) { defaultViewModelProviderFactory }

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var orderedFiles: List<FileEntity>
    private fun getFile(index: Int) =
        when {
            abs(index) >= orderedFiles.count() -> null
            index >= 0 -> orderedFiles[index]
            else -> orderedFiles[orderedFiles.count() + index]
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.files.value?.let { files ->
            val startingIndex = files.indexOfFirst { it.uri == viewModel.openFileUri.value }
            orderedFiles = files.drop(startingIndex) + files.take(startingIndex)
        }

        binding.imagesCarousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int = orderedFiles.count()
            val imageLoader = ImageLoader.Builder(requireContext())
                .crossfade(100)
                .build()
            override fun populate(view: View, index: Int) {
                (view as ImageView).load(getFile(index)?.uri, imageLoader)
                viewModel.closeFileUri.value = getFile(binding.imagesCarousel.currentIndex)?.uri
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
}