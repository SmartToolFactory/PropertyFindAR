package com.smarttoolfactory.core.di

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.hilt.lifecycle.ViewModelAssistedFactory
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import javax.inject.Provider

class DFMSavedStateViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle?,
    private val delegateFactory: SavedStateViewModelFactory,
    private val viewModelFactories:
        @JvmSuppressWildcards Map<String, Provider<ViewModelAssistedFactory<out ViewModel>>>,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @SuppressLint("RestrictedApi")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val factoryProvider = viewModelFactories[modelClass.name]
            ?: return delegateFactory.create("$KEY_PREFIX:$key", modelClass)
        @Suppress("UNCHECKED_CAST")
        return factoryProvider.get().create(handle) as T
    }

    companion object {
        private const val KEY_PREFIX = "androidx.hilt.lifecycle.HiltViewModelFactory"
    }
}
