package com.smarttoolfactory.home.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.smarttoolfactory.home.viewmodel.PropertyListFlowViewModelFactory
import com.smarttoolfactory.home.viewmodel.PropertyListRxJava3ViewModelFactory
import com.smarttoolfactory.home.viewmodel.PropertyListViewModelFlow
import com.smarttoolfactory.home.viewmodel.PropertyListViewModelRxJava3
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@InstallIn(FragmentComponent::class)
@Module
class HomeModule {

    @Provides
    fun providePropertyListViewModelFlow(
        fragment: Fragment,
        factory: PropertyListFlowViewModelFactory
    ) =
        ViewModelProvider(fragment, factory).get(PropertyListViewModelFlow::class.java)

    @Provides
    fun providePropertyListViewModelRxJava3(
        fragment: Fragment,
        factory: PropertyListRxJava3ViewModelFactory
    ) =
        ViewModelProvider(fragment, factory).get(PropertyListViewModelRxJava3::class.java)

    @Provides
    fun provideCoroutineScope() = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
}
