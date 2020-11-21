package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.MappableItemViewBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.SingleViewBinderAdapter
import com.smarttoolfactory.core.util.inflate
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.model.ChartSectionModel
import com.smarttoolfactory.dashboard.databinding.ItemChartSectionBinding

class ChartSectionViewBinder(
    private val onChartItemClicked: ((Float) -> Unit)? = null,
    private val pool: RecyclerView.RecycledViewPool? = null,
) : MappableItemViewBinder<ChartSectionModel, ChartSectionViewHolder>(
    ChartSectionModel::class.java
) {

    override fun createViewHolder(parent: ViewGroup): ChartSectionViewHolder {

        return ChartSectionViewHolder(
            parent.inflate(getItemLayoutResource()),
            onChartItemClicked,
            pool
        )
    }

    override fun bindViewHolder(
        model: ChartSectionModel,
        viewHolder: ChartSectionViewHolder
    ) {
        viewHolder.bind(model)
    }

    override fun getItemLayoutResource() = R.layout.item_chart_section

    override fun areItemsTheSame(
        oldItem: ChartSectionModel,
        newItem: ChartSectionModel
    ): Boolean {
        return oldItem.chartTitle == newItem.chartTitle
    }

    override fun areContentsTheSame(
        oldItem: ChartSectionModel,
        newItem: ChartSectionModel
    ): Boolean {
        return true
    }

    override fun onViewRecycled(viewHolder: ChartSectionViewHolder) {
        viewHolder.onViewRecycled()
        println("👻 Horizontal onViewRecycled: $viewHolder")
    }

    override fun onViewDetachedFromWindow(viewHolder: ChartSectionViewHolder) = Unit
}

@Suppress("UNCHECKED_CAST")
class ChartSectionViewHolder(
    private val binding: ItemChartSectionBinding,
    private val onChartItemClicked: ((Float) -> Unit)? = null,
    private val pool: RecyclerView.RecycledViewPool?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: ChartSectionModel) {

        if (model.items.isNullOrEmpty()) {
            binding.chartLayout.visibility = View.GONE
            binding.cardView.visibility = View.GONE
        } else {
            binding.chartLayout.visibility = View.VISIBLE
            binding.cardView.visibility = View.VISIBLE

            binding.tvType.text = model.chartTitle

            // ViewPager2
            val viewPager = binding.viewPager

            // TabLayout
            val tabLayout = binding.tabLayout

            viewPager.adapter =
                SingleViewBinderAdapter(BarChartViewBinder(onChartItemClicked) as ItemBinder)
                    .apply {
                        submitList(listOf(model, model))
                    }

            // Bind tabs and viewpager
            TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy).attach()
        }
    }

    internal fun onViewRecycled() = Unit

    private val tabConfigurationStrategy =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = "Indices"
                else -> tab.text = "Trends"
            }
        }
}
