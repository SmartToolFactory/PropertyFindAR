package com.smarttoolfactory.favorites.di

import androidx.fragment.app.Fragment
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.favorites.DashboardFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [CoreModuleDependencies::class],
    modules = [DashboardModule::class]
)
interface DashboardComponent {

    fun inject(fragment: DashboardFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependentModule: CoreModuleDependencies,
            @BindsInstance fragment: Fragment
        ): DashboardComponent
    }
}
