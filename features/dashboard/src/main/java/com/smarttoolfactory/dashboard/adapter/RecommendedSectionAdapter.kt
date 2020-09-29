package com.smarttoolfactory.dashboard.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.recyclerview.itemcallback.DefaultItemCallback
import com.smarttoolfactory.dashboard.adapter.model.RecommendedSectionModel
import com.smarttoolfactory.dashboard.adapter.viewholder.RecommendedSectionViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.RecommendedSectionViewHolder

class RecommendedSectionAdapter(private val gridSectionViewBinder: RecommendedSectionViewBinder) :
    ListAdapter<RecommendedSectionModel, RecommendedSectionViewHolder>(
        DefaultItemCallback()
    ) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun getItemViewType(position: Int): Int =
        gridSectionViewBinder.getItemLayoutResource()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendedSectionViewHolder {
        return gridSectionViewBinder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecommendedSectionViewHolder, position: Int) {
        gridSectionViewBinder.bindViewHolder(currentList[position], holder)
    }

    override fun onViewRecycled(holder: RecommendedSectionViewHolder) {
        gridSectionViewBinder.onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecommendedSectionViewHolder) {
        gridSectionViewBinder.onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }
}
