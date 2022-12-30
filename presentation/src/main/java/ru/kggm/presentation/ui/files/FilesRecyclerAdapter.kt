package ru.kggm.presentation.ui.files

import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import ru.kggm.presentation.R
import ru.kggm.presentation.databinding.RvItemFileBinding
import ru.kggm.presentation.entities.FileEntity

class FilesRecyclerAdapter
    : ListAdapter<FileEntity, FilesRecyclerAdapter.FileViewHolder>(FilesComparator()) {

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: RvItemFileBinding = DataBindingUtil.bind(itemView)!!

        fun bind(file: FileEntity) {
            Glide.with(itemView)
                .load(file.uri)
                .transition(withCrossFade(200))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
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

    class FilesComparator : DiffUtil.ItemCallback<FileEntity>() {
        override fun areItemsTheSame(oldItem: FileEntity, newItem: FileEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FileEntity, newItem: FileEntity): Boolean {
            return oldItem.uri == newItem.uri
                    && oldItem.name == newItem.name
        }
    }

    class OnClickListener(val clickListener: (file: FileEntity) -> Unit) {
        fun onClick(file: FileEntity) = clickListener(file)
    }
    private var onClickListener: OnClickListener? = null
    fun setOnFileClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : FileViewHolder = FileViewHolder.create(parent)

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onClickListener?.onClick(getItem(position)) }
        holder.bind(getItem(position))
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()
}