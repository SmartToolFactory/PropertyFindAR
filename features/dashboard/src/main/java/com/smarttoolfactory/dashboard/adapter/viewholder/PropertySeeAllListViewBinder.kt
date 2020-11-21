package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.BaseItemViewBinder
import com.smarttoolfactory.core.util.executeAfter
import com.smarttoolfactory.core.util.inflate
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.databinding.ItemPropertySeeAllBinding
import com.smarttoolfactory.domain.model.PropertyItem

/**
 * This ViewBinder cannot be used as multiple layout since it's model [PropertyItem]
 * is same with other ViewBinder
 */
class PropertySeeAllListViewBinder(
    private val onItemClicked: ((PropertyItem, ItemPropertySeeAllBinding) -> Unit)? = null
) : BaseItemViewBinder<PropertyItem, PropertySeeAllListItemViewHolder>() {

    override fun createViewHolder(parent: ViewGroup): PropertySeeAllListItemViewHolder {

        val binding = parent
            .inflate<ItemPropertySeeAllBinding>(getItemLayoutResource())

        return PropertySeeAllListItemViewHolder(binding, onItemClicked)
    }

    override fun bindViewHolder(
        model: PropertyItem,
        viewHolder: PropertySeeAllListItemViewHolder
    ) {
        viewHolder.bind(model)
    }

    override fun getItemLayoutResource() = R.layout.item_property_see_all

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

    override fun onViewRecycled(viewHolder: PropertySeeAllListItemViewHolder) {
        viewHolder.onViewRecycled()
        super.onViewRecycled(viewHolder)
    }
}

class PropertySeeAllListItemViewHolder(
    private val binding: ItemPropertySeeAllBinding,
    private val onItemClicked: ((PropertyItem, ItemPropertySeeAllBinding) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PropertyItem) {

        binding.executeAfter {

            this.item = item
            binding.cardView.transitionName = item.transitionName

            root.setOnClickListener {
                onItemClicked?.let { onItemClick ->
                    onItemClick(item, binding)
                }
            }
        }
    }

    fun onViewRecycled() {
        binding.root.setOnClickListener(null)
        binding.item = null
        binding.unbind()
    }
}
