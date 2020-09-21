package com.smarttoolfactory.core.ui.adapter

import androidx.recyclerview.widget.DiffUtil

/**
 * Base [DiffUtil.ItemCallback]  that calculates the difference between two lists
 * and outputs a list of update operations that converts the first list into the second one.
 *
 * * With data classes have same items in constructor will have same **hash code** so using
 * hash code for [DiffUtil.ItemCallback.areContentsTheSame]
 */
class DefaultItemDiffCallback<ItemType> : DiffUtil.ItemCallback<ItemType>() {

    override fun areItemsTheSame(
        oldItem: ItemType,
        newItem: ItemType
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: ItemType,
        newItem: ItemType
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}
