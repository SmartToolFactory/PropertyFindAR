package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.BaseItemViewBinder
import com.smarttoolfactory.core.util.clearResources
import com.smarttoolfactory.core.util.executeAfter
import com.smarttoolfactory.core.util.inflate
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.databinding.ItemRecommendedPropertyBinding
import com.smarttoolfactory.domain.model.PropertyItem

/**
 * This ViewBinder cannot be used as multiple layout since it's model [PropertyItem]
 * is same with other ViewBinder
 */
class RecommendedPropertyViewBinder(
    private val onItemClicked: ((PropertyItem) -> Unit)? = null,
) : BaseItemViewBinder<PropertyItem, RecommendedItemViewHolder>() {
    override fun createViewHolder(parent: ViewGroup): RecommendedItemViewHolder {
        return RecommendedItemViewHolder(
            parent.inflate(getItemLayoutResource()),
            onItemClicked,
        )
    }

    override fun bindViewHolder(
        model: PropertyItem,
        viewHolder: RecommendedItemViewHolder
    ) {
        viewHolder.bind(model)
    }

    override fun getItemLayoutResource() = R.layout.item_recommended_property

    override fun areItemsTheSame(
        oldItem: PropertyItem,
        newItem: PropertyItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PropertyItem,
        newItem: PropertyItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun onViewRecycled(viewHolder: RecommendedItemViewHolder) {
        viewHolder.onViewRecycled()
        super.onViewRecycled(viewHolder)
    }
}

class RecommendedItemViewHolder(
    private val binding: ItemRecommendedPropertyBinding,
    private val onItemClicked: ((PropertyItem) -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PropertyItem) {

        binding.executeAfter {

            this.item = item

            root.setOnClickListener {
                onItemClicked?.let { onItemClick ->
                    onItemClick(item)
                }
            }
        }
    }

    fun onViewRecycled() {
        binding.root.setOnClickListener(null)
        binding.ivThumbnail.clearResources()
        binding.item = null
        binding.unbind()
    }
}
