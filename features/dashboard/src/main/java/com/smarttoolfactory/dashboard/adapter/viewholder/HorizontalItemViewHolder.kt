package com.smarttoolfactory.dashboard.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.dashboard.BR
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.PropertyListAdapter
import com.smarttoolfactory.dashboard.adapter.layoutmanager.ScaledLinearLayoutManager
import com.smarttoolfactory.dashboard.databinding.ItemListWithTitleBinding
import com.smarttoolfactory.dashboard.model.HorizontalTitleListModel

class HorizontalItemViewHolder(
    private val binding: ItemListWithTitleBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(item: HorizontalTitleListModel) {

        binding.setVariable(BR.horizontalListModel, item)

        binding.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                ScaledLinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

            // Set RecyclerViewAdapter
            val itemListAdapter = PropertyListAdapter(R.layout.item_property_favorite)

            this.adapter = itemListAdapter
        }

        binding.executePendingBindings()
    }
}
