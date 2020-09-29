package com.smarttoolfactory.home.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.di.qualifier.RecycledViewPool
import com.smarttoolfactory.home.propertylist.flow.PropertyListViewModel
import com.smarttoolfactory.home.propertylist.paged.PagedPropertyListViewModel
import com.smarttoolfactory.home.propertylist.rxjava.PropertyListViewModelRxJava3
import com.smarttoolfactory.home.viewmodel.PagedPropertyListViewModelFactory
import com.smarttoolfactory.home.viewmodel.PropertyListFlowViewModelFactory
import com.smarttoolfactory.home.viewmodel.PropertyListRxJava3ViewModelFactory
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

    /**
     * Property ViewModel that uses Flow for data operation
     */
    @Provides
    fun providePropertyListViewModelFlow(
        fragment: Fragment,
        factory: PropertyListFlowViewModelFactory
    ) =
        ViewModelProvider(fragment, factory).get(PropertyListViewModel::class.java)

    /**
     * Property ViewModel that uses Flow for data operation with Pagination
     */
    @Provides
    fun providePagedPropertyListViewModel(
        fragment: Fragment,
        factory: PagedPropertyListViewModelFactory
    ) =
        ViewModelProvider(fragment, factory).get(PagedPropertyListViewModel::class.java)

    /**
     * Property ViewModel that uses Rxjava for data operations
     */
    @Provides
    fun providePropertyListViewModelRxJava3(
        fragment: Fragment,
        factory: PropertyListRxJava3ViewModelFactory
    ) =
        ViewModelProvider(fragment, factory).get(PropertyListViewModelRxJava3::class.java)

    @Provides
    fun provideCoroutineScope() = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    /**
     * Shared RecycledViewPool to lower number of inflation counts in inner RecyclerViews
     * that use same Views
     */
    @RecycledViewPool(value = RecycledViewPool.Type.PROPERTY_ITEM)
    @Provides
    fun providePropertyItemRecycledViewPool() = RecyclerView.RecycledViewPool()
}
