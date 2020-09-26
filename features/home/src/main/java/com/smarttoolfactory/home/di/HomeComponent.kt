package com.smarttoolfactory.home.di

import androidx.fragment.app.Fragment
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.home.propertylist.flow.PropertyListFragment
import com.smarttoolfactory.home.propertylist.paged.PagedPropertyListFragment
import com.smarttoolfactory.home.propertylist.rxjava.PropertyListFragmentRxJava3
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [CoreModuleDependencies::class],
    modules = [HomeModule::class]
)
interface HomeComponent {

    fun inject(fragment: PropertyListFragment)
    fun inject(fragment: PropertyListFragmentRxJava3)
    fun inject(fragment: PagedPropertyListFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependentModule: CoreModuleDependencies,
            @BindsInstance fragment: Fragment
        ): HomeComponent
    }
}
