package com.smarttoolfactory.core.ui.recyclerview.itemcallback

import androidx.recyclerview.widget.DiffUtil
import com.smarttoolfactory.domain.model.PropertyItem

class PropertyItemCallback : DiffUtil.ItemCallback<PropertyItem>() {

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
        return oldItem.hashCode() == newItem.hashCode()
    }
}
