package com.smarttoolfactory.property_detail.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.smarttoolfactory.property_detail.PropertyDetailViewModel
import com.smarttoolfactory.property_detail.PropertyDetailViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@InstallIn(FragmentComponent::class)
@Module
class PropertyDetailModule {

    @Provides
    fun providePostDetailViewModel(fragment: Fragment, factory: PropertyDetailViewModelFactory) =
        ViewModelProvider(fragment, factory).get(PropertyDetailViewModel::class.java)

    @Provides
    fun provideCoroutineScope() = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
}
