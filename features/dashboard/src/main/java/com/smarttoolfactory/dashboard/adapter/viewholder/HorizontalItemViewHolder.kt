package com.smarttoolfactory.dashboard.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.dashboard.BR
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.PropertyListAdapter
import com.smarttoolfactory.dashboard.adapter.layoutmanager.ScaledLinearLayoutManager
import com.smarttoolfactory.dashboard.databinding.LayoutListWithTitleBinding
import com.smarttoolfactory.dashboard.model.PropertyItemListModel
import com.smarttoolfactory.domain.model.PropertyItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HorizontalItemViewHolder(
    private val binding: LayoutListWithTitleBinding,
    private val onItemClick: ((PropertyItem) -> Unit)? = null,
    private val onSeeAllClick: ((PropertyItemListModel) -> Unit)? = null,
    private val coroutineScope: CoroutineScope? = null,
    private val scrollPositionStateFlow: MutableStateFlow<Int>? = null
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(item: PropertyItemListModel) {

        binding.setVariable(BR.propertyListModel, item)

        // Set RecyclerView
        binding.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                ScaledLinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

            // Set RecyclerViewAdapter
            val itemListAdapter = PropertyListAdapter(R.layout.item_property_favorite, onItemClick)

            this.adapter = itemListAdapter
        }

        // Set see all touch listener
        binding.tvSeeAll.setOnClickListener {
            onSeeAllClick?.let {
                it(item)
            }
        }

        binding.executePendingBindings()

        listenScrollStateFlow()
    }

    private fun listenScrollStateFlow() {
        coroutineScope?.let {
            scrollPositionStateFlow
                ?.onEach { position ->
                    binding.recyclerView.smoothScrollToPosition(position)
                }
                ?.launchIn(coroutineScope)
        }
    }
}
