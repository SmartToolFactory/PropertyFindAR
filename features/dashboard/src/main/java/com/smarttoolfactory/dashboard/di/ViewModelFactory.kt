
package com.smarttoolfactory.dashboard.di

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

interface ViewModelFactory<out V : ViewModel> {
    fun create(handle: SavedStateHandle): V
}

class GenericSavedStateViewModelFactory<out V : ViewModel>(
    private val viewModelFactory: ViewModelFactory<V>,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return viewModelFactory.create(handle) as T
    }
}

/**
 * Convenience function to use with `by viewModels` that creates an instance of
 * [AbstractSavedStateViewModelFactory] that enables us to pass [SavedStateHandle]
 * to the [ViewModel]'s constructor.
 *
 * @param factory instance of [ViewModelFactory] that will be used to construct the [ViewModel]
 * @param owner instance of Fragment or Activity that owns the [ViewModel]
 * @param defaultArgs Bundle with default values to populate the [SavedStateHandle]
 *
 * @see ViewModelFactory
 */
@MainThread
inline fun <reified VM : ViewModel> SavedStateRegistryOwner.withFactory(
    factory: ViewModelFactory<VM>,
    defaultArgs: Bundle? = null
) = GenericSavedStateViewModelFactory(factory, this, defaultArgs)
