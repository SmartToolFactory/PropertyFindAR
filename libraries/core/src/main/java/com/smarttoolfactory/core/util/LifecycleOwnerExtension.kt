package com.smarttoolfactory.core.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.observe(liveData: LiveData<T?>, predicate: (T) -> Unit) {
    liveData.observe(this, Observer { it?.let { predicate(it) } })
}
