package com.smarttoolfactory.core.ui.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractItemViewBinder<ItemType, in VH : RecyclerView.ViewHolder>(
    val position: ItemViewType,
    viewHolder: VH
) : DiffUtil.ItemCallback<ItemType>() {

    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun bindViewHolder(item: ItemType, viewHolder: VH)

    fun getItemLayout(): Int = getItemViewType().layoutRes

    open fun bindViewHolder(
        item: ItemType?,
        viewHolder: VH,
        payloads: MutableList<Any>
    ) = Unit

    /**
     * Type of the ViewHolder. Can be header, footer, data, or any custom type
     * that should be used
     */
    abstract fun getItemViewType(): ItemViewType

    open fun onViewRecycled(viewHolder: VH) = Unit
    open fun onViewDetachedFromWindow(viewHolder: VH) = Unit
}

enum class ItemViewType(val type: Int, @LayoutRes val layoutRes: Int = -1) {

    VERTICAL_ITEM_TYPE(0),
    HORIZONTAL_ITEM_TYPE(1)
}
