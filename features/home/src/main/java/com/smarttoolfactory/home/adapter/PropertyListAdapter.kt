package com.smarttoolfactory.home.adapter

import android.widget.ImageButton
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.adapter.BaseListAdapter
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.home.BR
import com.smarttoolfactory.home.R
import com.smarttoolfactory.home.databinding.RowPropertyBinding

class PropertyItemListAdapter(
    @LayoutRes private val layoutId: Int,
    private val onItemClicked: ((PropertyItem) -> Unit)? = null,
    private val onLikeButtonClicked: ((PropertyItem) -> Unit)? = null
) :
    BaseListAdapter<PropertyItem>(
        layoutId,
        PropertyItemDiffCallback()
    ) {

    override fun onViewHolderBound(
        binding: ViewDataBinding,
        item: PropertyItem,
        position: Int,
        payloads: MutableList<Any>
    ) {
        binding.setVariable(BR.item, item)
    }

    /**
     * Add click listener here to prevent setting listener after a ViewHolder every time
     * ViewHolder is scrolled and onBindViewHolder is called
     */
    override fun onViewHolderCreated(
        viewHolder: RecyclerView.ViewHolder,
        viewType: Int,
        binding: ViewDataBinding
    ) {

        binding.root.setOnClickListener {
            onItemClicked?.let {
                it((getItem(viewHolder.bindingAdapterPosition)))
            }
        }

        if (binding is RowPropertyBinding) {

            binding.ivLike.setOnClickListener { likeButton ->
                onLikeButtonClicked?.let { onLikeButtonClick ->

                    getItem(viewHolder.bindingAdapterPosition).apply {
                        // Change like status of PropertyItem
                        isFavorite = !isFavorite
                        onLikeButtonClick(this)

                        // Set image source of like button
                        val imageResource = if (isFavorite) {
                            R.drawable.ic_baseline_favorite_24
                        } else {
                            R.drawable.ic_baseline_favorite_border_24
                        }
                        (likeButton as? ImageButton)?.setImageResource(imageResource)
                    }
                }
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class PropertyItemDiffCallback : DiffUtil.ItemCallback<PropertyItem>() {

    override fun areItemsTheSame(oldItem: PropertyItem, newItem: PropertyItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PropertyItem, newItem: PropertyItem): Boolean {
        return oldItem == newItem
    }
}
