package com.smarttoolfactory.dashboard.adapter.layoutmanager

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Layout manager for displaying last item visible partially after total item set
 */
class ScaledHorizontalGridLayoutManager constructor(
    context: Context?,
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = RecyclerView.HORIZONTAL,
    reverseLayout: Boolean = false,
    private val totalItems: Int = 1,
) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {

    private val partialVisibilityRatio = 0.3f

    private val horizontalSpace get() = width - paddingStart - paddingEnd

//    override fun smoothScrollToPosition(
//        recyclerView: RecyclerView,
//        state: RecyclerView.State?,
//        position: Int
//    ) {
//        val snappedScroller = TopSnappedSmoothScroller(recyclerView.context)
//
//        snappedScroller.targetPosition = position
//        startSmoothScroll(snappedScroller)
//
//
//    }

    override fun generateDefaultLayoutParams() =
        scaledLayoutParams(super.generateDefaultLayoutParams())

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?) =
        scaledLayoutParams(super.generateLayoutParams(lp))

    override fun generateLayoutParams(c: Context?, attrs: AttributeSet?) =
        scaledLayoutParams(super.generateLayoutParams(c, attrs))

    private fun scaledLayoutParams(layoutParams: RecyclerView.LayoutParams) =
        layoutParams.apply {
            val marginBetweenItems =
                (layoutParams.marginStart + layoutParams.marginEnd) * itemCount

            val oneItemRatio = ((100 / (totalItems.toFloat() + partialVisibilityRatio)) / 100)
            width = (oneItemRatio * (horizontalSpace - 0)).toInt()
        }

//    private inner class TopSnappedSmoothScroller(context: Context?) :
//        LinearSmoothScroller(context) {
//
//        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
//            return this@ScaledHorizontalGridLayoutManager.computeScrollVectorForPosition(
//                targetPosition
//            )
//        }
//
//        override fun getVerticalSnapPreference(): Int {
//            return SNAP_TO_START
//        }
//
//        override fun getHorizontalSnapPreference(): Int {
//            return SNAP_TO_START
//        }
//    }
}
