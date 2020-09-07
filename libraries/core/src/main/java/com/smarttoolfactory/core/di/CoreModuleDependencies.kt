package com.smarttoolfactory.core.di

import com.smarttoolfactory.domain.usecase.GetPropertiesUseCaseFlow
import com.smarttoolfactory.domain.usecase.GetPropertiesUseCaseRxJava3
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * This component is required for adding component to Dynamic Feature Module dependencies
 */
@EntryPoint
@InstallIn(ApplicationComponent::class)
interface CoreModuleDependencies {

    /*
     Provision methods to provide dependencies to components that depend on this component
   */
    fun getPropertiesUseCaseFlow(): GetPropertiesUseCaseFlow
    fun getPropertiesUseCaseRxJava3(): GetPropertiesUseCaseRxJava3
}
