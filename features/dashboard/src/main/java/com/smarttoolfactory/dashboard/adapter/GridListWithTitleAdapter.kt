package com.smarttoolfactory.dashboard.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.recyclerview.itemcallback.DefaultItemCallback
import com.smarttoolfactory.dashboard.adapter.viewholder.GridItemViewHolder
import com.smarttoolfactory.dashboard.adapter.viewholder.GridSectionViewBinder
import com.smarttoolfactory.dashboard.model.GridPropertyListModel

class GridListWithTitleAdapter(private val gridSectionViewBinder: GridSectionViewBinder) :
    ListAdapter<GridPropertyListModel, GridItemViewHolder>(
        DefaultItemCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {
        return gridSectionViewBinder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
        holder.bindTo(currentList[position])
    }

    override fun onViewRecycled(holder: GridItemViewHolder) {
        gridSectionViewBinder.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: GridItemViewHolder) {
        gridSectionViewBinder.onViewDetachedFromWindow(holder)
    }
}
