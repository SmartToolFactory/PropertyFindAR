package com.smarttoolfactory.core.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessScrollListener(
    private val linearLayoutManager: LinearLayoutManager,
    private val listener: ScrollToBottomListener
) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 8
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    fun onRefresh() {
        previousTotal = 0
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = linearLayoutManager.itemCount
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount
            <= firstVisibleItem + visibleThreshold
        ) {
            listener.onScrollToBottom()
            loading = true
        }
    }

    interface ScrollToBottomListener {
        fun onScrollToBottom()
    }
}
