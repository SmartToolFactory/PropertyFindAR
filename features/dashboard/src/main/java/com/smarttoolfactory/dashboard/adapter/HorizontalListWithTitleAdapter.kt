package com.smarttoolfactory.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.adapter.DefaultItemDiffCallback
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.viewholder.HorizontalItemViewHolder
import com.smarttoolfactory.dashboard.model.PropertyListWithTitleModel

class HorizontalListWithTitleAdapter :
    ListAdapter<PropertyListWithTitleModel, HorizontalItemViewHolder>(
        DefaultItemDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalItemViewHolder {

        return HorizontalItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_list_with_title_see,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HorizontalItemViewHolder, position: Int) {
        holder.bindTo(currentList[position])
    }
}
