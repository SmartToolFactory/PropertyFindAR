package com.smarttoolfactory.core.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.domain.model.Item

// TODO NOT FINISH THIS ADAPTER
class GenericListAdapter<T : Item>(
    private val viewBinders: List<AbstractItemViewBinder<T, RecyclerView.ViewHolder>>
) : ListAdapter<T, RecyclerView.ViewHolder>(DefaultItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        viewBinders[position].getItemViewType().type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return viewBinders[viewType].createViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        viewBinders[position]
            .bindViewHolder(currentList.getOrNull(position), holder, payloads)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return viewBinders[holder.itemViewType].bindViewHolder(getItem(position), holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        viewBinders[holder.itemViewType].onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        viewBinders[holder.itemViewType].onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }
}
