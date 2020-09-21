package com.smarttoolfactory.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.adapter.DefaultItemDiffCallback
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.viewholder.VerticalItemViewHolder
import com.smarttoolfactory.dashboard.model.PropertyListWithTitleModel

class VerticalListWithTitleAdapter :
    ListAdapter<PropertyListWithTitleModel, VerticalItemViewHolder>(
        DefaultItemDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalItemViewHolder {

        return VerticalItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_list_with_title,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: VerticalItemViewHolder, position: Int) {
        holder.bindTo(currentList[position])
    }
}
