package com.smarttoolfactory.dashboard.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.recyclerview.itemcallback.DefaultItemCallback
import com.smarttoolfactory.dashboard.adapter.viewholder.BarChartViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.BarChartViewHolder
import com.smarttoolfactory.dashboard.model.ChartItemListModel

class BarChartAdapter(private val barChartViewBinder: BarChartViewBinder) :
    ListAdapter<ChartItemListModel, BarChartViewHolder>(
        DefaultItemCallback()
    ) {

    override fun getItemViewType(position: Int): Int =
        barChartViewBinder.getItemLayoutResource()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarChartViewHolder {
        return barChartViewBinder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BarChartViewHolder, position: Int) {
        barChartViewBinder.bindViewHolder(currentList[position], holder)
    }

    override fun onViewRecycled(holder: BarChartViewHolder) {
        barChartViewBinder.onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: BarChartViewHolder) {
        barChartViewBinder.onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }
}
