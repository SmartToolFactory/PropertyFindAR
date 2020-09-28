package com.smarttoolfactory.core.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}

inline fun <reified T : ViewDataBinding> ViewGroup.inflate(
    @LayoutRes layout: Int,
    attachToRoot: Boolean = false
): T = DataBindingUtil.inflate<T>(
    LayoutInflater.from(context),
    layout,
    this,
    attachToRoot
)

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
