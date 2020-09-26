package com.smarttoolfactory.dashboard.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.SingleLayoutListAdapter
import com.smarttoolfactory.core.ui.recyclerview.itemcallback.PropertyItemCallback
import com.smarttoolfactory.dashboard.BR
import com.smarttoolfactory.domain.model.PropertyItem

class PropertyListAdapter(
    @LayoutRes private val layoutId: Int,
    private val onItemClicked: ((PropertyItem) -> Unit)? = null,
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
    }
}
