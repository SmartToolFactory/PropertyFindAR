package com.smarttoolfactory.dashboard.adapter.viewholder

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.dashboard.BR
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.PropertyListAdapter
import com.smarttoolfactory.dashboard.adapter.layoutmanager.ScaledHorizontalGridLayoutManager
import com.smarttoolfactory.dashboard.databinding.LayoutListWithTitleBinding
import com.smarttoolfactory.dashboard.model.PropertyItemListModel
import com.smarttoolfactory.domain.model.PropertyItem

/**
 * ViewHolder for displaying properties with columns of 3 elements that can be scrolled
 * horizontally.
 */
class GridItemViewHolder(
    private val binding: LayoutListWithTitleBinding,
    private val onItemClick: ((PropertyItem) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(item: PropertyItemListModel) {

        binding.setVariable(BR.propertyListModel, item)

        binding.recyclerView.apply {

            val snapHelper = LinearSnapHelper()

            snapHelper.attachToRecyclerView(this)

            // Set Layout manager
            this.layoutManager =
                ScaledHorizontalGridLayoutManager(
                    context = this.context,
                    spanCount = 3,
                    orientation = GridLayoutManager.HORIZONTAL,
                    reverseLayout = false
                )

            // Set RecyclerViewAdapter
            val itemListAdapter =
                PropertyListAdapter(R.layout.item_property_recommended, onItemClick)

            this.adapter = itemListAdapter
        }

        binding.executePendingBindings()
    }
}
