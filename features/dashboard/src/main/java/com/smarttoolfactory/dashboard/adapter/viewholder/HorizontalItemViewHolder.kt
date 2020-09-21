package com.smarttoolfactory.dashboard.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.dashboard.BR
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.PropertyListAdapter
import com.smarttoolfactory.dashboard.adapter.layoutmanager.ScaledLinearLayoutManager
import com.smarttoolfactory.dashboard.databinding.LayoutListWithTitleSeeBinding
import com.smarttoolfactory.dashboard.model.PropertyListWithTitleModel
import com.smarttoolfactory.domain.model.PropertyItem

class HorizontalItemViewHolder(
    private val binding: LayoutListWithTitleSeeBinding,
    private val onItemClick: ((PropertyItem) -> Unit)? = null
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(item: PropertyListWithTitleModel) {

        binding.setVariable(BR.propertyListModel, item)

        binding.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                ScaledLinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

            // Set RecyclerViewAdapter
            val itemListAdapter = PropertyListAdapter(R.layout.item_property_favorite, onItemClick)

            this.adapter = itemListAdapter
        }

        binding.executePendingBindings()
    }
}
