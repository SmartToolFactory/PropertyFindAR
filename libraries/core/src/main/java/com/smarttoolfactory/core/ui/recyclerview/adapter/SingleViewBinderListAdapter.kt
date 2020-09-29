package com.smarttoolfactory.core.ui.recyclerview.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.smarttoolfactory.core.ui.recyclerview.itemcallback.DefaultItemCallback

/**
 * RecyclerView adapter for setting list with one type of layout using [BaseItemViewBinder].
 *
 * [BaseItemViewBinder] class is responsible for creation of [ViewHolder], and handling
 * lifecycle of [ViewHolder].
 *
 */
class SingleViewBinderAdapter(
    private val viewBinder: ItemBinder,
    stateRestorationPolicy: StateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY,
    private val recycleChildrenOnDetach: Boolean = false
) : ListAdapter<Any, ViewHolder>(DefaultItemCallback()) {

    init {
        this.stateRestorationPolicy = stateRestorationPolicy
    }

    override fun getItemViewType(position: Int): Int =
        viewBinder.getItemLayoutResource()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recycleChildrenOnDetach) {
            val layoutManager = recyclerView.layoutManager
            (layoutManager as? LinearLayoutManager)?.recycleChildrenOnDetach = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return viewBinder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewBinder.bindViewHolder(currentList[position], holder)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        viewBinder.onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        viewBinder.onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }
}

internal class SingleTypeDiffCallback(
    private val viewBinder: ItemBinder
) : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return viewBinder?.areItemsTheSame(oldItem, newItem) ?: false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        // We know the items are the same class because [areItemsTheSame] returned true
        return viewBinder?.areContentsTheSame(oldItem, newItem) ?: false
    }
}
