package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.dashboard.BR
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.PropertyListAdapter
import com.smarttoolfactory.dashboard.adapter.layoutmanager.ScaledHorizontalGridLayoutManager
import com.smarttoolfactory.dashboard.databinding.LayoutListWithTitleBinding
import com.smarttoolfactory.dashboard.model.PropertyListWithTitleModel
import com.smarttoolfactory.domain.model.PropertyItem

class VerticalItemViewHolder(
    private val binding: LayoutListWithTitleBinding,
    private val onItemClick: ((PropertyItem) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(item: PropertyListWithTitleModel) {

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
