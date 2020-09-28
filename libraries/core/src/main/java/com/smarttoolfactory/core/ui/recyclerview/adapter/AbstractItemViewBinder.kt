package com.smarttoolfactory.core.ui.recyclerview.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractItemViewBinder<M, in VH : RecyclerView.ViewHolder>(
    val modelClazz: Class<out M>
) : DiffUtil.ItemCallback<M>() {

    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindViewHolder(model: M, viewHolder: VH)
    abstract fun getItemLayoutResource(): Int

    // Having these as non abstract because not all the viewBinders are required to implement them.
    open fun onViewRecycled(viewHolder: VH) = Unit
    open fun onViewDetachedFromWindow(viewHolder: VH) = Unit
}
