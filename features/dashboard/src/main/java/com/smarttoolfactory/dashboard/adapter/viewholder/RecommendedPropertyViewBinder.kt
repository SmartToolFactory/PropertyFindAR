package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.AbstractItemViewBinder
import com.smarttoolfactory.dashboard.DashboardViewModel
import com.smarttoolfactory.dashboard.databinding.ItemRecommendedPropertyBinding
import com.smarttoolfactory.dashboard.model.RecommendedItemModel

class RecommendedPropertyViewBinder(
    private val viewModel: DashboardViewModel
) : AbstractItemViewBinder<RecommendedItemModel, RecommendedItemViewHolder>(
    RecommendedItemModel::class.java
) {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun bindViewHolder(
        model: RecommendedItemModel,
        viewHolder: RecommendedItemViewHolder
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemLayoutResource(): Int {
        TODO("Not yet implemented")
    }

    override fun areItemsTheSame(
        oldItem: RecommendedItemModel,
        newItem: RecommendedItemModel
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(
        oldItem: RecommendedItemModel,
        newItem: RecommendedItemModel
    ): Boolean {
        TODO("Not yet implemented")
    }
}

class RecommendedItemViewHolder(val binding: ItemRecommendedPropertyBinding) :
    RecyclerView.ViewHolder(binding.root)
