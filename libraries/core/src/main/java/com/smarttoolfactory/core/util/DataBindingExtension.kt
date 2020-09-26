package com.smarttoolfactory.core.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}

inline fun <reified T : ViewDataBinding> T.inflateWith(
    parent: ViewGroup,
    @LayoutRes layoutId: Int
) {
    DataBindingUtil.inflate<T>(
        LayoutInflater.from(parent.context),
        layoutId,
        parent, false
    )
}
