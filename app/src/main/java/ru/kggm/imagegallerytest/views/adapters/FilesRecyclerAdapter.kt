package ru.kggm.imagegallerytest.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.imagegallerytest.R
import com.example.imagegallerytest.databinding.RvItemFileBinding
import ru.kggm.imagegallerytest.data.entities.File

class FilesRecyclerAdapter
    : ListAdapter<File, FilesRecyclerAdapter.FileViewHolder>(FilesComparator()) {
    private var onClickListener: OnClickListener? = null
    fun setFileOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: RvItemFileBinding = DataBindingUtil.bind(itemView)!!

        fun bind(file: File) {
//            ImageRequest.Builder(itemView.context)
//                .target(binding.thumbnail)
//                .crossfade(true)
//                .data(file.uri)
//                .build()
//                .also {
//                    itemView.context.imageLoader.enqueue(it)
//                }
            Glide.with(itemView)
                .load(file.uri)
                .transition(withCrossFade())
                .into(binding.thumbnail)
        }

        companion object {
            fun create(parent: ViewGroup): FileViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_file, parent, false)
                return FileViewHolder(view)
            }
        }
    }

    class FilesComparator : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.uri == newItem.uri
                    && oldItem.name == newItem.name
        }
    }

    class OnClickListener(val clickListener: (file: File) -> Unit) {
        fun onClick(file: File) = clickListener(file)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : FileViewHolder = FileViewHolder.create(parent)

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onClickListener?.onClick(getItem(position)) }
        holder.bind(getItem(position))
    }
}