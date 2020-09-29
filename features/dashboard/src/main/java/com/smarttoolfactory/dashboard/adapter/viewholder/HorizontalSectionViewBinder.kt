package com.smarttoolfactory.dashboard.adapter.viewholder

import android.os.Parcelable
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.MappableItemViewBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.SingleViewBinderAdapter
import com.smarttoolfactory.core.util.executeAfter
import com.smarttoolfactory.core.util.inflate
import com.smarttoolfactory.dashboard.DashboardViewModel
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.layoutmanager.ScaledLinearLayoutManager
import com.smarttoolfactory.dashboard.adapter.model.PropertyListModel
import com.smarttoolfactory.dashboard.databinding.ItemPropertySectionHorizontalBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HorizontalSectionViewBinder(
    private val viewModel: DashboardViewModel,
    private val pool: RecyclerView.RecycledViewPool? = null,
    var layoutManagerState: Parcelable? = null,
    private val coroutineScope: CoroutineScope? = null,
    private val scrollPositionStateFlow: MutableStateFlow<Int>? = null
) :
    MappableItemViewBinder<PropertyListModel, HorizontalSectionViewHolder>(
        PropertyListModel::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): HorizontalSectionViewHolder {

        val holder = HorizontalSectionViewHolder(
            parent.inflate(getItemLayoutResource()),
            viewModel,
            pool,
            coroutineScope,
            scrollPositionStateFlow
        )

        println("😂 HorizontalSectionViewBinder createViewHolder() $holder")

//        holder.binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    saveInstanceState(holder)
//                }
//            }
//        })

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
    private val viewModel: DashboardViewModel,
    private val pool: RecyclerView.RecycledViewPool?,
    private val coroutineScope: CoroutineScope? = null,
    private val scrollPositionStateFlow: MutableStateFlow<Int>? = null
) :
    RecyclerView.ViewHolder(binding.root) {

    private var layoutManager: RecyclerView.LayoutManager? = null

    internal fun getLayoutManagerState(): Parcelable? = layoutManager?.onSaveInstanceState()

    fun bind(model: PropertyListModel, layoutManagerState: Parcelable?) {

        println("🚕 HorizontalSectionViewHolder bind() holder: $this")

        binding.executeAfter {

            propertyListModel = model
            this.viewModel = this@HorizontalSectionViewHolder.viewModel

            // Set RecyclerView
            recyclerView.apply {

                // Set shared RecycledView Pool
                setRecycledViewPool(pool)

                // Set Layout manager
                this.layoutManager =
                    ScaledLinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                        .apply {
                            recycleChildrenOnDetach = true
                        }

                // Click listener for items
                val onItemClick = viewModel?.let { it::onItemClick }

                // Set RecyclerViewAdapter
                val itemListAdapter =
                    SingleViewBinderAdapter(HorizontalPropertyViewBinder(onItemClick) as ItemBinder)

//                val itemListAdapter =
//                    PropertyListAdapter(R.layout.item_property_horizontal, onItemClick)

                this.adapter = itemListAdapter

//                itemAnimator = null
//                isNestedScrollingEnabled = false

                pool?.let {
                    this.setRecycledViewPool(pool)
                }
            }

            layoutManager = binding.recyclerView.layoutManager

            // 🔥 Restore layout state before onDestroy or configuration change
            if (layoutManagerState != null) {
                layoutManager?.onRestoreInstanceState(layoutManagerState)
            }

            // Set see all touch listener
            tvSeeAll.setOnClickListener {
                viewModel?.let {
                    (it::onSeeAllClick)(model)
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
        binding.viewModel = null
        binding.propertyListModel = null
        binding.unbind()
    }
}
