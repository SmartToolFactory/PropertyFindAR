package com.smarttoolfactory.dashboard.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.recyclerview.itemcallback.DefaultItemCallback
import com.smarttoolfactory.dashboard.adapter.model.PropertyListModel
import com.smarttoolfactory.dashboard.adapter.viewholder.HorizontalSectionViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.HorizontalSectionViewHolder

class HorizontalSectionAdapter(
    private val horizontalSectionViewBinder: HorizontalSectionViewBinder
) : ListAdapter<PropertyListModel, HorizontalSectionViewHolder>(
    DefaultItemCallback()
) {
    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun getItemViewType(position: Int): Int =
        horizontalSectionViewBinder.getItemLayoutResource()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizontalSectionViewHolder {
        return horizontalSectionViewBinder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: HorizontalSectionViewHolder, position: Int) {
        horizontalSectionViewBinder.bindViewHolder(currentList[position], holder)
    }

    override fun onViewRecycled(holder: HorizontalSectionViewHolder) {
        horizontalSectionViewBinder.onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: HorizontalSectionViewHolder) {
        horizontalSectionViewBinder.onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }
}
