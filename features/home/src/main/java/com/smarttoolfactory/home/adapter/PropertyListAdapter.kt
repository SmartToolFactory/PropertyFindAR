package com.smarttoolfactory.home.adapter

import android.widget.ImageButton
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.SingleLayoutListAdapter
import com.smarttoolfactory.core.ui.recyclerview.itemcallback.PropertyItemCallback
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.home.BR
import com.smarttoolfactory.home.databinding.ItemPropertyListBinding

class PropertyListAdapter(
    @LayoutRes private val layoutId: Int,
    private val onItemClicked: ((PropertyItem) -> Unit)? = null,
    private val onLikeButtonClicked: ((PropertyItem) -> Unit)? = null
) :
    SingleLayoutListAdapter<PropertyItem>(
        layoutId,
        PropertyItemCallback()
    ) {

    override fun onViewHolderBound(
        binding: ViewDataBinding,
        item: PropertyItem,
        position: Int,
        payloads: MutableList<Any>
    ) {
        binding.setVariable(BR.item, item)
    }

    /**
     * Add click listener here to prevent setting listener after a ViewHolder every time
     * ViewHolder is scrolled and onBindViewHolder is called
     */
    override fun onViewHolderCreated(
        viewHolder: RecyclerView.ViewHolder,
        viewType: Int,
        binding: ViewDataBinding
    ) {

        binding.root.setOnClickListener {
            onItemClicked?.let {
                it((getItem(viewHolder.bindingAdapterPosition)))
            }
        }

        if (binding is ItemPropertyListBinding) {

            binding.ivLike.setOnClickListener { likeButton ->

                onLikeButtonClicked?.let { onLikeButtonClick ->

                    getItem(viewHolder.bindingAdapterPosition).apply {
                        // Change like status of PropertyItem
                        isFavorite = !isFavorite
                        onLikeButtonClick(this)

                        // Set AnimatedVectorDrawable for like button
                        (likeButton as? ImageButton)?.apply {

                            val stateSet =
                                intArrayOf(android.R.attr.state_checked * if (isFavorite) 1 else -1)
                            setImageState(stateSet, true)
                        }
                    }
                }
            }
        }
    }
}
