package com.smarttoolfactory.core.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors

/**
 * Abstract [ListAdapter] with **ItemType** for data type.
 *
 * * In [onViewHolderBound] function, this function is called when list is scrolled either, it's
 * required to set [ViewDataBinding.setVariable], also it's possible to set adapter behavior
 * based on **payloads** in this function.
 *
 * * In [onViewHolderCreated] function, this method called when new data is required,
 * set any required click listeners for root view or other row elements.
 */
abstract class SingleLayoutListAdapter<ItemType>(
    @LayoutRes private val layoutId: Int,
    callBack: DiffUtil.ItemCallback<ItemType> = DefaultItemDiffCallback(),
) : ListAdapter<ItemType, BaseItemViewHolder>(
    AsyncDifferConfig.Builder<ItemType>(callBack)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseItemViewHolder {

        return BaseItemViewHolder(

            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent, false
            )
        ).apply {
            onViewHolderCreated(this, viewType, binding)
        }
    }

    override fun onBindViewHolder(
        holder: BaseItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        val item: ItemType? = currentList.getOrNull(position)

        item?.let {
            // Add   holder.binding.setVariable(BR.item, item) in extending Adapter
            onViewHolderBound(holder.binding, item, position, payloads)
            holder.binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: BaseItemViewHolder, position: Int) {
        println("🔥🔥")
        Log.d(SingleLayoutListAdapter::class.toString(), "onBindViewHolder")
    }

    /**
     * Called when a ViewHolder is created. ViewHolder is either created first time or
     * when data is refreshed.
     *
     * This method is not called when RecyclerView is being scrolled
     */
    open fun onViewHolderCreated(
        viewHolder: RecyclerView.ViewHolder,
        viewType: Int,
        binding: ViewDataBinding
    ) {
    }

    /**
     * bind view while RecyclerView is being scrolled and new items are bound
     */
    open fun onViewHolderBound(
        binding: ViewDataBinding,
        item: ItemType,
        position: Int,
        payloads: MutableList<Any>
    ) {
    }
}

open class BaseItemViewHolder(
    val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root)
