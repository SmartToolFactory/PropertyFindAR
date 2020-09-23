package com.smarttoolfactory.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.adapter.DefaultItemDiffCallback
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.viewholder.BarChartViewHolder
import com.smarttoolfactory.dashboard.model.ChartItemListModel

class BarChartAdapter(private val onChartItemClicked: ((Float) -> Unit)? = null) :
    ListAdapter<ChartItemListModel, BarChartViewHolder>(
        DefaultItemDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarChartViewHolder {

        return BarChartViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_bar_chart,
                parent, false
            ),
            onChartItemClicked
        )
    }

    override fun onBindViewHolder(holder: BarChartViewHolder, position: Int) {
        holder.bindTo(currentList[position])
    }
}
