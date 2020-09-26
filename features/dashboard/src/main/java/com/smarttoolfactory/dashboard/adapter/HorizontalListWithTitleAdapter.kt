package com.smarttoolfactory.dashboard.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.recyclerview.itemcallback.DefaultItemCallback
import com.smarttoolfactory.dashboard.adapter.viewholder.HorizontalPropertySectionViewHolder
import com.smarttoolfactory.dashboard.adapter.viewholder.HorizontalSectionViewBinder
import com.smarttoolfactory.dashboard.model.PropertyListModel

class HorizontalListWithTitleAdapter(
    private val horizontalSectionViewBinder: HorizontalSectionViewBinder
) : ListAdapter<PropertyListModel, HorizontalPropertySectionViewHolder>(
    DefaultItemCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizontalPropertySectionViewHolder {
        return horizontalSectionViewBinder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: HorizontalPropertySectionViewHolder, position: Int) {
        horizontalSectionViewBinder.bindViewHolder(currentList[position], holder)
    }

    override fun onViewRecycled(holder: HorizontalPropertySectionViewHolder) {
        horizontalSectionViewBinder.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: HorizontalPropertySectionViewHolder) {
        horizontalSectionViewBinder.onViewDetachedFromWindow(holder)
    }
}
