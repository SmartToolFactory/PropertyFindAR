package com.smarttoolfactory.core.util

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide

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

fun ImageView.clearResources() {
    if ((context as? AppCompatActivity)?.isDestroyed == true) return
    Glide.with(context).clear(this)
    setImageDrawable(null)
}
