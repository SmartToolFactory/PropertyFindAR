package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.AbstractItemViewBinder
import com.smarttoolfactory.core.util.executeAfter
import com.smarttoolfactory.dashboard.BR
import com.smarttoolfactory.dashboard.DashboardViewModel
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.PropertyListAdapter
import com.smarttoolfactory.dashboard.adapter.layoutmanager.ScaledHorizontalGridLayoutManager
import com.smarttoolfactory.dashboard.databinding.ItemPropertySectionGridBinding
import com.smarttoolfactory.dashboard.model.GridPropertyListModel
import com.smarttoolfactory.domain.model.PropertyItem

class GridSectionViewBinder(
    private val viewModel: DashboardViewModel,
    private val onItemClick: ((PropertyItem) -> Unit)? = null
) : AbstractItemViewBinder<GridPropertyListModel, GridItemViewHolder>(
    GridPropertyListModel::class.java
) {

    override fun createViewHolder(parent: ViewGroup): GridItemViewHolder {
        return GridItemViewHolder(
            ItemPropertySectionGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel,
            onItemClick
        )
    }

    override fun bindViewHolder(model: GridPropertyListModel, viewHolder: GridItemViewHolder) {
        viewHolder.bindTo(model)
    }

    override fun getItemLayoutResource(): Int = R.layout.item_property_section_grid

    override fun areItemsTheSame(
        oldItem: GridPropertyListModel,
        newItem: GridPropertyListModel
    ): Boolean {
        return oldItem.items.hashCode() == newItem.items.hashCode()
    }

    override fun areContentsTheSame(
        oldItem: GridPropertyListModel,
        newItem: GridPropertyListModel
    ): Boolean {
        return false
    }
}

/**
 * ViewHolder for displaying properties with columns of 3 elements that can be scrolled
 * horizontally.
 */
class GridItemViewHolder(
    private val binding: ItemPropertySectionGridBinding,
    private val viewModel: DashboardViewModel,
    private val onItemClick: ((PropertyItem) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(item: GridPropertyListModel) {

        binding.executeAfter {

            setVariable(BR.propertyListModel, item)
            setVariable(BR.viewModel, viewModel)

            recyclerView.apply {

                val snapHelper = LinearSnapHelper()

                /*
                Setting this not to null causes exception
                https://stackoverflow.com/questions/44043501/an-instance-of-onflinglistener-already-set-in-recyclerview
             */
                onFlingListener = null
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
        }
    }
}
