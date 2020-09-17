package com.smarttoolfactory.core.di

import com.smarttoolfactory.domain.usecase.property.GetPropertiesUseCaseFlow
import com.smarttoolfactory.domain.usecase.property.GetPropertiesUseCasePaged
import com.smarttoolfactory.domain.usecase.property.GetPropertiesUseCaseRxJava3
import com.smarttoolfactory.domain.usecase.property.SetPropertyStatsUseCase
import com.smarttoolfactory.domain.usecase.property.SetPropertyStatsUseCaseRxJava3
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * This component is required for adding dependencies to Dynamic Feature Modules by
 * adding [CoreModule] as dependent component
 */
@EntryPoint
@InstallIn(ApplicationComponent::class)
interface CoreModuleDependencies {

    /*
     Provision methods to provide dependencies to components that depend on this component
   */
    fun getPropertiesUseCaseFlow(): GetPropertiesUseCaseFlow
    fun getPropertiesUseCaseRxJava3(): GetPropertiesUseCaseRxJava3
    fun getPropertiesUseCasePaged(): GetPropertiesUseCasePaged

    // Set property like or view status
    fun setPropertyStatsUseCase(): SetPropertyStatsUseCase
    fun setPropertyStatsUseCaseRxJava3(): SetPropertyStatsUseCaseRxJava3
}
