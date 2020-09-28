package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.AbstractItemViewBinder
import com.smarttoolfactory.dashboard.R

// Shown while loading
object LoadingIndicator

class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class LoadingViewBinder : AbstractItemViewBinder<LoadingIndicator, LoadingViewHolder>(
    LoadingIndicator::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingViewHolder(
            LayoutInflater.from(parent.context).inflate(getItemLayoutResource(), parent, false)
        )
    }

    override fun bindViewHolder(model: LoadingIndicator, viewHolder: LoadingViewHolder) {
        Toast.makeText(
            viewHolder.itemView.context,
            "LoadingViewBinder bindViewHolder() $viewHolder",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun getItemLayoutResource() = R.layout.item_loading

    override fun areItemsTheSame(oldItem: LoadingIndicator, newItem: LoadingIndicator) = true

    override fun areContentsTheSame(oldItem: LoadingIndicator, newItem: LoadingIndicator) = true
}
