package com.smarttoolfactory.dashboard.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.adapter.SingleLayoutListAdapter
import com.smarttoolfactory.dashboard.BR
import com.smarttoolfactory.domain.model.PropertyItem

class PropertyListAdapter(
    @LayoutRes private val layoutId: Int,
    private val onItemClicked: ((PropertyItem) -> Unit)? = null
) :
    SingleLayoutListAdapter<PropertyItem>(
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
