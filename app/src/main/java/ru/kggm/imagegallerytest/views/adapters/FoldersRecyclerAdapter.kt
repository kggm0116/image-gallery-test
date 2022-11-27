package ru.kggm.imagegallerytest.views.adapters

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
import com.example.imagegallerytest.R
import com.example.imagegallerytest.databinding.RvItemFolderBinding
import ru.kggm.imagegallerytest.data.entities.Folder

class FoldersRecyclerAdapter
    : ListAdapter<Folder, FoldersRecyclerAdapter.FolderViewHolder>(FoldersComparator()) {
    var selectionTracker: SelectionTracker<Uri>? = null

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: RvItemFolderBinding = DataBindingUtil.bind(itemView)!!

        fun bind(folder: Folder, isActivated: Boolean = false){
            binding.folder = folder
            itemView.isActivated = isActivated
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Uri> =
            object : ItemDetailsLookup.ItemDetails<Uri>() {
                override fun getPosition(): Int = absoluteAdapterPosition
                override fun getSelectionKey(): Uri? = binding.folder?.uri // Should probably be different
            }

        companion object {
            fun create(parent: ViewGroup): FolderViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_folder, parent, false)
                return FolderViewHolder(view)
            }
        }
    }

    class FoldersComparator : DiffUtil.ItemCallback<Folder>() {
        override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
            return oldItem.uri == newItem.uri
                    && oldItem.name == newItem.name
        }
    }

    class OnClickListener(val clickListener: (folder: Folder) -> Unit) {
        fun onClick(folder: Folder) = clickListener(folder)
    }
    private var onClickListener: OnClickListener? = null
    fun setFolderOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : FolderViewHolder = FolderViewHolder.create(parent)

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = getItem(position)
        holder.itemView.setOnClickListener { onClickListener?.onClick(folder) }
        selectionTracker?.let {
            holder.bind(folder, it.isSelected(folder.uri))
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    class FolderKeyProvider(private val adapter: FoldersRecyclerAdapter) : ItemKeyProvider<Uri>(SCOPE_CACHED)
    {
        override fun getKey(position: Int) = adapter.currentList[position].uri
        override fun getPosition(key: Uri) = adapter.currentList.indexOfFirst {it.uri == key}
    }

    class FolderDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Uri>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<Uri>? {
            val view = recyclerView.findChildViewUnder(event.x, event.y)
            if (view != null)
                    return (recyclerView.getChildViewHolder(view) as FolderViewHolder).getItemDetails()
            return null
        }
    }
}