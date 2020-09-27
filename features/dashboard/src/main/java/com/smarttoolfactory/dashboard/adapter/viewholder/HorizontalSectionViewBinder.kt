package com.smarttoolfactory.dashboard.adapter.viewholder

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.AbstractItemViewBinder
import com.smarttoolfactory.core.util.executeAfter
import com.smarttoolfactory.dashboard.BR
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.PropertyListAdapter
import com.smarttoolfactory.dashboard.adapter.layoutmanager.ScaledLinearLayoutManager
import com.smarttoolfactory.dashboard.databinding.ItemPropertySectionHorizontalBinding
import com.smarttoolfactory.dashboard.model.PropertyListModel
import com.smarttoolfactory.domain.model.PropertyItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HorizontalSectionViewBinder(
    private val onItemClick: ((PropertyItem) -> Unit)? = null,
    private val onSeeAllClick: ((PropertyListModel) -> Unit)? = null,
    var layoutManagerState: Parcelable? = null,
    private val coroutineScope: CoroutineScope? = null,
    private val scrollPositionStateFlow: MutableStateFlow<Int>? = null
) :
    AbstractItemViewBinder<PropertyListModel, HorizontalSectionViewHolder>(
        PropertyListModel::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): HorizontalSectionViewHolder {

        val holder = HorizontalSectionViewHolder(
            ItemPropertySectionHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick,
            onSeeAllClick,
            coroutineScope,
            scrollPositionStateFlow
        )

        println("😂 HorizontalSectionViewBinder createViewHolder() $holder")

        holder.binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    saveInstanceState(holder)
                }
            }
        })

        return holder
    }

    override fun bindViewHolder(
        model: PropertyListModel,
        viewHolder: HorizontalSectionViewHolder
    ) {
        viewHolder.bind(model, layoutManagerState)
    }

    override fun getItemLayoutResource() = R.layout.item_property_section_horizontal

    override fun areItemsTheSame(
        oldItem: PropertyListModel,
        newItem: PropertyListModel
    ): Boolean {
        return oldItem.items == newItem.items
    }

    override fun areContentsTheSame(
        oldItem: PropertyListModel,
        newItem: PropertyListModel
    ): Boolean {
        return true
    }

    override fun onViewRecycled(viewHolder: HorizontalSectionViewHolder) {
        saveInstanceState(viewHolder)
        viewHolder.onViewRecycled()
        println("👻 Horizontal onViewRecycled: $viewHolder")
    }

    override fun onViewDetachedFromWindow(viewHolder: HorizontalSectionViewHolder) {
        saveInstanceState(viewHolder)
        println("💀 Horizontal onViewDetachedFromWindow: $viewHolder")
    }

    private fun saveInstanceState(viewHolder: HorizontalSectionViewHolder) {
        if (viewHolder.bindingAdapterPosition == RecyclerView.NO_POSITION) {
            return
        }
        layoutManagerState = viewHolder.getLayoutManagerState()
    }
}

class HorizontalSectionViewHolder(
    internal val binding: ItemPropertySectionHorizontalBinding,
    private val onItemClick: ((PropertyItem) -> Unit)? = null,
    private val onSeeAllClick: ((PropertyListModel) -> Unit)? = null,
    private val coroutineScope: CoroutineScope? = null,
    private val scrollPositionStateFlow: MutableStateFlow<Int>? = null
) :
    RecyclerView.ViewHolder(binding.root) {

    private var layoutManager: RecyclerView.LayoutManager? = null

    internal fun getLayoutManagerState(): Parcelable? = layoutManager?.onSaveInstanceState()

    fun bind(item: PropertyListModel, layoutManagerState: Parcelable?) {

        println("🚕 HorizontalSectionViewHolder bind() holder: $this")

        binding.executeAfter {

            setVariable(BR.propertyListModel, item)

            // Set RecyclerView
            recyclerView.apply {

                // Set Layout manager
                this.layoutManager =
                    ScaledLinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

                // Set RecyclerViewAdapter
                val itemListAdapter =
                    PropertyListAdapter(R.layout.item_property_favorite, onItemClick)

                this.adapter = itemListAdapter

//                itemAnimator = null
//                isNestedScrollingEnabled = false
            }

            layoutManager = binding.recyclerView.layoutManager

            // Restore layout state to previous one
            if (layoutManagerState != null) {
                layoutManager?.onRestoreInstanceState(layoutManagerState)
            }

            // Set see all touch listener
            tvSeeAll.setOnClickListener {
                onSeeAllClick?.let {
                    it(item)
                }
            }
        }

//        listenScrollStateFlow()
    }

    internal fun listenScrollStateFlow() {
        coroutineScope?.let {
            scrollPositionStateFlow
                ?.onEach { position ->
                    binding.recyclerView.smoothScrollToPosition(position)
                }
                ?.launchIn(coroutineScope)
        }
    }

    internal fun onViewRecycled() {
        binding.tvSeeAll.setOnClickListener(null)
        binding.unbind()
    }
}
