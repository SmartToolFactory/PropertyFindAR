package com.smarttoolfactory.dashboard.adapter.viewholder

import android.os.Parcelable
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.MappableItemViewBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.SingleViewBinderAdapter
import com.smarttoolfactory.core.util.executeAfter
import com.smarttoolfactory.core.util.inflate
import com.smarttoolfactory.dashboard.DashboardViewModel
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.layoutmanager.ScaledHorizontalGridLayoutManager
import com.smarttoolfactory.dashboard.adapter.model.RecommendedSectionModel
import com.smarttoolfactory.dashboard.databinding.ItemRecommendedSectionBinding

class RecommendedSectionViewBinder(
    private val viewModel: DashboardViewModel,
    var layoutManagerState: Parcelable? = null
) : MappableItemViewBinder<RecommendedSectionModel, RecommendedSectionViewHolder>(
    RecommendedSectionModel::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecommendedSectionViewHolder {

        val holder = RecommendedSectionViewHolder(
            parent.inflate(getItemLayoutResource()),
            viewModel
        )

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
        model: RecommendedSectionModel,
        viewHolder: RecommendedSectionViewHolder
    ) {
        viewHolder.bind(model, layoutManagerState)
    }

    override fun getItemLayoutResource(): Int = R.layout.item_recommended_section

    override fun areItemsTheSame(
        oldItem: RecommendedSectionModel,
        newItem: RecommendedSectionModel
    ): Boolean {
        return oldItem.items.hashCode() == newItem.items.hashCode()
    }

    override fun areContentsTheSame(
        oldItem: RecommendedSectionModel,
        newItem: RecommendedSectionModel
    ): Boolean {
        return false
    }

    override fun onViewRecycled(viewHolder: RecommendedSectionViewHolder) {
        saveInstanceState(viewHolder)
        viewHolder.onViewRecycled()
        println("👻 RecommendedSectionViewBinder onViewRecycled: $viewHolder")
    }

    override fun onViewDetachedFromWindow(viewHolder: RecommendedSectionViewHolder) {
        saveInstanceState(viewHolder)
        println("💀 RecommendedSectionViewBinder onViewDetachedFromWindow: $viewHolder")
    }

    private fun saveInstanceState(viewHolder: RecommendedSectionViewHolder) {
        if (viewHolder.bindingAdapterPosition == RecyclerView.NO_POSITION) {
            return
        }
        layoutManagerState = viewHolder.getLayoutManagerState()
    }
}

/**
 * ViewHolder for displaying properties with columns of 3 elements that can be scrolled
 * horizontally.
 */
class RecommendedSectionViewHolder(
    internal val binding: ItemRecommendedSectionBinding,
    private val viewModel: DashboardViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    private var layoutManager: RecyclerView.LayoutManager? = null

    internal fun getLayoutManagerState(): Parcelable? = layoutManager?.onSaveInstanceState()

    fun bind(model: RecommendedSectionModel, layoutManagerState: Parcelable?) {

        println("🚌 RecommendedSectionViewHolder bind() holder: $this")

        binding.executeAfter {

            binding.recommendedSectionModel = model
            binding.viewModel = this@RecommendedSectionViewHolder.viewModel

            recyclerView.apply {

                val snapHelper = PagerSnapHelper()

                /*
                Setting this NOT to null causes exception
                https://stackoverflow.com/questions/44043501/an-instance-of-onflinglistener-already-set-in-recyclerview
             */
                onFlingListener = null
                snapHelper.attachToRecyclerView(this)

                // Set Layout manager
                this.layoutManager =
                    ScaledHorizontalGridLayoutManager(
                        context = this.context,
                        spanCount = 3,
                        orientation = GridLayoutManager.HORIZONTAL,
                        reverseLayout = false
                    )

                // Set RecyclerViewAdapter

                val onItemClick = viewModel?.let { it::onItemClick }

                val itemListAdapter =
                    SingleViewBinderAdapter(
                        RecommendedPropertyViewBinder(onItemClick) as ItemBinder
                    )

                this.adapter = itemListAdapter

//                itemAnimator = null
                // Causing scroll to stagger at the beginning in horizontal items
//                isNestedScrollingEnabled = false
            }

            layoutManager = binding.recyclerView.layoutManager

            if (layoutManagerState != null) {
                layoutManager?.onRestoreInstanceState(layoutManagerState)
            }
        }
    }

    internal fun onViewRecycled() {
        binding.root.setOnClickListener(null)
        binding.viewModel = null
        binding.recommendedSectionModel = null
        binding.unbind()
    }
}
