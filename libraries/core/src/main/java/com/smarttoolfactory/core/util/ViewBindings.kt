package com.smarttoolfactory.core.util

import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * [BindingAdapter]s for the binding items to ListAdapter.
 */
@BindingAdapter("app:items")
fun RecyclerView.setItems(items: List<Nothing>?) {

    items?.let {
        (adapter as ListAdapter<*, *>)?.submitList(items)
    }
}

/**
 * Display or hide a view based on a condition
 *
 * @param condition if it's true this View's visibility is set to [View.VISIBLE]
 */
@BindingAdapter("visibilityBasedOn")
fun View.visibilityBasedOn(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

@BindingAdapter("showWhen")
fun ContentLoadingProgressBar.showWhen(condition: Boolean) {
    if (condition) show() else hide()
}
