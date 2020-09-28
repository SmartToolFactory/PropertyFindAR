package com.smarttoolfactory.dashboard.di

import androidx.recyclerview.widget.RecyclerView
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

    @Provides
    fun provideCoroutineScope() = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    /*
        Shared RecycledViewPool to lower number of inflation counts in inner RecyclerViews
        that use same Views
     */
    @RecycledViewPoolQualifier(value = RecycledViewPoolQualifier.Type.PROPERTY_HORIZONTAL)
    @Provides
    fun provideHorizontalRecycledViewPool() = RecyclerView.RecycledViewPool()

    @RecycledViewPoolQualifier(value = RecycledViewPoolQualifier.Type.PROPERTY_ITEM)
    @Provides
    fun providePropertyItemRecycledViewPool() = RecyclerView.RecycledViewPool()

    @RecycledViewPoolQualifier(value = RecycledViewPoolQualifier.Type.CHART_ITEM)
    @Provides
    fun provideChartItemRecycledViewPool() = RecyclerView.RecycledViewPool()
}
