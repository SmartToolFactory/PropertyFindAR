package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.AbstractItemViewBinder
import com.smarttoolfactory.dashboard.DashboardViewModel
import com.smarttoolfactory.dashboard.databinding.ItemRecommendedPropertyBinding
import com.smarttoolfactory.dashboard.model.PropertyItemModel

class HorizontalPropertyViewBinder(
    private val viewModel: DashboardViewModel
) : AbstractItemViewBinder<PropertyItemModel, HorizontalPropertyViewHolder>(
    PropertyItemModel::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun bindViewHolder(
        model: PropertyItemModel,
        viewHolder: HorizontalPropertyViewHolder
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemLayoutResource(): Int {
        TODO("Not yet implemented")
    }

    override fun areItemsTheSame(oldItem: PropertyItemModel, newItem: PropertyItemModel): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(
        oldItem: PropertyItemModel,
        newItem: PropertyItemModel
    ): Boolean {
        TODO("Not yet implemented")
    }
}

class HorizontalPropertyViewHolder(val binding: ItemRecommendedPropertyBinding) :
    RecyclerView.ViewHolder(binding.root)
