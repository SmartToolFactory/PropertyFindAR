package com.smarttoolfactory.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.adapter.DefaultItemDiffCallback
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.viewholder.ChartContainerViewHolder
import com.smarttoolfactory.dashboard.model.ChartItemModel

class ChartContainerAdapter :
    ListAdapter<ChartItemModel, ChartContainerViewHolder>(
        DefaultItemDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartContainerViewHolder {

        return ChartContainerViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_chart_container,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ChartContainerViewHolder, position: Int) {
        holder.bindTo(currentList[position])
    }
}
