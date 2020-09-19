package com.smarttoolfactory.dashboard.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.smarttoolfactory.dashboard.DashboardViewModel
import com.smarttoolfactory.dashboard.DashboardViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@InstallIn(FragmentComponent::class)
@Module
class DashboardModule {

    /**
     * Property ViewModel that uses Rxjava for data operations
     */
    @Provides
    fun provideDashboardViewModel(
        fragment: Fragment,
        factory: DashboardViewModelFactory
    ) =
        ViewModelProvider(fragment, factory).get(DashboardViewModel::class.java)

    @Provides
    fun provideCoroutineScope() = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
}
