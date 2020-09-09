package com.smarttoolfactory.property_detail.di

import androidx.fragment.app.Fragment
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.property_detail.PropertyDetailFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [CoreModuleDependencies::class],
    modules = [PropertyDetailModule::class]
)
interface PropertyDetailComponent {

    fun inject(fragment: PropertyDetailFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependentModule: CoreModuleDependencies,
            @BindsInstance fragment: Fragment
        ): PropertyDetailComponent
    }
}
