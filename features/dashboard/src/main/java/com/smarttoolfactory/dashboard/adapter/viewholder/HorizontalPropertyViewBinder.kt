package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.BaseItemViewBinder
import com.smarttoolfactory.core.util.clearResources
import com.smarttoolfactory.core.util.executeAfter
import com.smarttoolfactory.core.util.inflate
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.databinding.ItemPropertyHorizontalBinding
import com.smarttoolfactory.domain.model.PropertyItem

/**
 * This ViewBinder cannot be used as multiple layout since it's model [PropertyItem]
 * is same with other ViewBinder
 */
class HorizontalPropertyViewBinder(
    private val onItemClicked: ((PropertyItem) -> Unit)? = null,
) : BaseItemViewBinder<PropertyItem, HorizontalItemViewHolder>() {

    override fun createViewHolder(parent: ViewGroup): HorizontalItemViewHolder {

        val holder = HorizontalItemViewHolder(
            parent.inflate(getItemLayoutResource()),
            onItemClicked,
        )

        println("🍔 HorizontalPropertyViewBinder createViewHolder() $holder")

        return holder
    }

    override fun bindViewHolder(
        model: PropertyItem,
        viewHolder: HorizontalItemViewHolder
    ) {
        println("🍜 HorizontalPropertyViewBinder bindViewHolder() $viewHolder")

        viewHolder.bind(model)
    }

    override fun getItemLayoutResource() = R.layout.item_property_horizontal

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

    override fun onViewRecycled(viewHolder: HorizontalItemViewHolder) {
        viewHolder.onViewRecycled()
        super.onViewRecycled(viewHolder)
    }
}

class HorizontalItemViewHolder(
    private val binding: ItemPropertyHorizontalBinding,
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
        println("👻 HorizontalPropertyViewBinder onViewRecycled()")
        binding.root.setOnClickListener(null)
        binding.ivBanner.clearResources()
        binding.item = null
        binding.unbind()
    }
}
