package com.smarttoolfactory.home.di

import androidx.fragment.app.Fragment
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.home.propertylist.PagedPropertyListFragment
import com.smarttoolfactory.home.propertylist.PropertyListFlowFragment
import com.smarttoolfactory.home.propertylist.PropertyListRxjava3Fragment
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [CoreModuleDependencies::class],
    modules = [HomeModule::class]
)
interface HomeComponent {

    fun inject(fragment: PropertyListFlowFragment)
    fun inject(fragment: PropertyListRxjava3Fragment)
    fun inject(fragment: PagedPropertyListFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependentModule: CoreModuleDependencies,
            @BindsInstance fragment: Fragment
        ): HomeComponent
    }
}
