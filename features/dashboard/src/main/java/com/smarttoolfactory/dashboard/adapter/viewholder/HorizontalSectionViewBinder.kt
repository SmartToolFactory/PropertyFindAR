package com.smarttoolfactory.dashboard.adapter.viewholder

import android.os.Parcelable
import android.view.View
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

        println("🤔 HorizontalSectionViewBinder bindViewHolder() $viewHolder")

        viewHolder.bind(model, layoutManagerState)
    }

    override fun getItemLayoutResource() = R.layout.item_property_section_horizontal

    override fun areItemsTheSame(
        oldItem: PropertyListModel,
        newItem: PropertyListModel
    ): Boolean {
        return oldItem.title == newItem.title
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
        println("👻 HorizontalSectionViewBinder onViewRecycled: $viewHolder")
    }

    override fun onViewDetachedFromWindow(viewHolder: HorizontalSectionViewHolder) {
        saveInstanceState(viewHolder)
        println("💀 HorizontalSectionViewBinder onViewDetachedFromWindow: $viewHolder")
    }

    private fun saveInstanceState(viewHolder: HorizontalSectionViewHolder) {
        if (viewHolder.bindingAdapterPosition == RecyclerView.NO_POSITION) {
            return
        }
        layoutManagerState = viewHolder.getLayoutManagerState()
        println("😍 Horizontal saveInstance() $layoutManagerState")
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

        if (model.items.isNullOrEmpty()) {
            binding.horizontalSectionLayout.visibility = View.GONE
        } else {
            binding.horizontalSectionLayout.visibility = View.VISIBLE
            setUpHorizontalList(model, layoutManagerState)
        }

//        listenScrollStateFlow()
    }

    private fun setUpHorizontalList(
        model: PropertyListModel,
        layoutManagerState: Parcelable?
    ) {
        binding.executeAfter {

            propertyListModel = model
            this.viewModel = this@HorizontalSectionViewHolder.viewModel

            // Set RecyclerView
            recyclerView.apply {

                // Set Layout manager
                this.layoutManager =
                    ScaledLinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                        .apply {
                            pool?.let {
                                // Set shared RecycledView Pool
                                setRecycledViewPool(pool)
                                recycleChildrenOnDetach = true
                            }
                        }

                // Click listener for items
                val onItemClick = viewModel?.let { it::onItemClick }

                // Set RecyclerViewAdapter
                val itemListAdapter =
                    SingleViewBinderAdapter(HorizontalPropertyViewBinder(onItemClick) as ItemBinder)

                this.adapter = itemListAdapter

                //                itemAnimator = null
                //                isNestedScrollingEnabled = false
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
