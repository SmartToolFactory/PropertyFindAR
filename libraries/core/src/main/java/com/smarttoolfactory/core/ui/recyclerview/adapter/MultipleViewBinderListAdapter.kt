package com.smarttoolfactory.core.ui.recyclerview.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

typealias ItemClazz = Class<out Any>

typealias ItemBinder = AbstractItemViewBinder<Any, ViewHolder>

/**
 * RecyclerView adapter for setting list with different layouts using [AbstractItemViewBinder].
 *
 * Takes [Map] of model class [ItemClazz] and [ViewHolder] and transforms that
 * map to [viewTypeToBinders] with keys that point to layouts
 * returned from [AbstractItemViewBinder.getItemLayoutResource].
 *
 * Mapping happens between Model::class.java <-> ViewBinder
 * and between R.layout.RES <-> ViewBinder to glue from model class to layout getItemType
 * checks class of data which is send to this adapter with [ListAdapter.submitList].
 *
 */
class MultipleViewBinderListAdapter(
    private val viewBinders: Map<ItemClazz, ItemBinder>,
    stateRestorationPolicy: StateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
) : ListAdapter<Any, ViewHolder>(ItemDiffCallback(viewBinders)) {

    init {
        this.stateRestorationPolicy = stateRestorationPolicy
    }

    private val viewTypeToBinders = viewBinders.mapKeys { it.value.getItemLayoutResource() }

    private fun getViewBinder(viewType: Int): ItemBinder = viewTypeToBinders.getValue(viewType)

    override fun getItemViewType(position: Int): Int =
        viewBinders.getValue(super.getItem(position).javaClass).getItemLayoutResource()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return getViewBinder(viewType).createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return getViewBinder(getItemViewType(position)).bindViewHolder(getItem(position), holder)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        getViewBinder(holder.itemViewType).onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        getViewBinder(holder.itemViewType).onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }
}
