package com.smarttoolfactory.home.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.BaseItemViewBinder
import com.smarttoolfactory.core.util.executeAfter
import com.smarttoolfactory.core.util.inflate
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.home.R
import com.smarttoolfactory.home.databinding.ItemPropertyListBinding

/**
 * This ViewBinder cannot be used as multiple layout since it's model [PropertyItem]
 * is same with other ViewBinder
 */
class PropertyListViewBinder(
    private val onItemClicked: ((PropertyItem, ItemPropertyListBinding) -> Unit)? = null,
    private val onLikeButtonClicked: ((PropertyItem) -> Unit)? = null
) : BaseItemViewBinder<PropertyItem, PropertyListItemViewHolder>() {

    override fun createViewHolder(parent: ViewGroup): PropertyListItemViewHolder {

        val binding = parent
            .inflate<ItemPropertyListBinding>(getItemLayoutResource())

        return PropertyListItemViewHolder(binding, onItemClicked, onLikeButtonClicked)
    }

    override fun bindViewHolder(
        model: PropertyItem,
        viewHolder: PropertyListItemViewHolder
    ) {
        viewHolder.bind(model)
    }

    override fun getItemLayoutResource() = R.layout.item_property_list

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

    override fun onViewRecycled(viewHolder: PropertyListItemViewHolder) {
        viewHolder.onViewRecycled()
        super.onViewRecycled(viewHolder)
    }
}

class PropertyListItemViewHolder(
    private val binding: ItemPropertyListBinding,
    private val onItemClicked: ((PropertyItem, ItemPropertyListBinding) -> Unit)? = null,
    private val onLikeButtonClicked: ((PropertyItem) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PropertyItem) {

        binding.executeAfter {

            this.item = item
            binding.cardView.transitionName = item.id.toString()

            root.setOnClickListener {
                onItemClicked?.let { onItemClick ->
                    onItemClick(item, binding)
                }
            }
        }

        binding.ivLike.setOnClickListener { likeButton ->

            onLikeButtonClicked?.let { onLikeButtonClick ->

                item.apply {

                    // Change like status of PropertyItem
                    isFavorite = !isFavorite
                    onLikeButtonClick(this)

                    // Set AnimatedVectorDrawable for like button
                    binding.ivLike.apply {

                        val stateSet =
                            intArrayOf(android.R.attr.state_checked * if (isFavorite) 1 else -1)
                        setImageState(stateSet, true)
                    }
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
