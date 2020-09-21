package com.smarttoolfactory.dashboard.adapter.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

// TODO Create generic ViewHolder and Adapter for Multiple Layouts in one Adapter
abstract class AbstractViewHolder<ItemType, DataBinding : ViewDataBinding>(
    internal val binding: DataBinding
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bindTo(item: ItemType)
}
