package com.smarttoolfactory.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.smarttoolfactory.core.ui.adapter.DefaultItemDiffCallback
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.viewholder.HorizontalItemViewHolder
import com.smarttoolfactory.dashboard.model.PropertyItemListModel
import com.smarttoolfactory.domain.model.PropertyItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class HorizontalListWithTitleAdapter(
    private val onItemClick: ((PropertyItem) -> Unit)? = null,
    private val onSeeAllClick: ((PropertyItemListModel) -> Unit)? = null,
    private val coroutineScope: CoroutineScope? = null,
    private val scrollPositionStateFlow: MutableStateFlow<Int>? = null

) :
    ListAdapter<PropertyItemListModel, HorizontalItemViewHolder>(
        DefaultItemDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalItemViewHolder {

        return HorizontalItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_list_with_title,
                parent, false
            ),
            onItemClick,
            onSeeAllClick,
            coroutineScope,
            scrollPositionStateFlow
        )
    }

    override fun onBindViewHolder(holder: HorizontalItemViewHolder, position: Int) {
        holder.bindTo(currentList[position])
    }
}
