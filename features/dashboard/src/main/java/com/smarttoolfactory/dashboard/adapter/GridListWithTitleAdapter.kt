package com.smarttoolfactory.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.adapter.DefaultItemDiffCallback
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.viewholder.GridItemViewHolder
import com.smarttoolfactory.dashboard.model.PropertyItemListModel
import com.smarttoolfactory.domain.model.PropertyItem

class GridListWithTitleAdapter(private val onItemClick: ((PropertyItem) -> Unit)? = null) :
    ListAdapter<PropertyItemListModel, GridItemViewHolder>(
        DefaultItemDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {

        return GridItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_list_with_title,
                parent, false
            ),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
        holder.bindTo(currentList[position])
    }
}
