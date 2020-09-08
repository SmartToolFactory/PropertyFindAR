package com.smarttoolfactory.core.di

import com.smarttoolfactory.data.di.DatabaseModule
import com.smarttoolfactory.data.di.NetworkModule
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.repository.PagedPropertyRepository
import com.smarttoolfactory.data.repository.PagedPropertyRepositoryImpl
import com.smarttoolfactory.data.repository.PropertyRepositoryCoroutines
import com.smarttoolfactory.data.repository.PropertyRepositoryImlRxJava3
import com.smarttoolfactory.data.repository.PropertyRepositoryImplCoroutines
import com.smarttoolfactory.data.repository.PropertyRepositoryRxJava3
import com.smarttoolfactory.data.source.LocalDataSourceRxJava3Impl
import com.smarttoolfactory.data.source.LocalPagedPropertyDataSource
import com.smarttoolfactory.data.source.LocalPagedPropertySourceImpl
import com.smarttoolfactory.data.source.LocalPropertyDataSourceCoroutines
import com.smarttoolfactory.data.source.LocalPropertyDataSourceImpl
import com.smarttoolfactory.data.source.LocalPropertyDataSourceRxJava3
import com.smarttoolfactory.data.source.RemoteDataSourceRxJava3Impl
import com.smarttoolfactory.data.source.RemotePropertyDataSourceCoroutines
import com.smarttoolfactory.data.source.RemotePropertyDataSourceCoroutinesImpl
import com.smarttoolfactory.data.source.RemotePropertyDataSourceRxJava3
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module(includes = [DataProviderModule::class, NetworkModule::class, DatabaseModule::class])
interface DataModule {

    /*
        Coroutines
     */
    @Singleton
    @Binds
    fun bindRemoteDataSourceCoroutines(remoteDataSource: RemotePropertyDataSourceCoroutinesImpl):
        RemotePropertyDataSourceCoroutines

    @Singleton
    @Binds
    fun bindLocalDataSourceCoroutines(localDataSource: LocalPropertyDataSourceImpl):
        LocalPropertyDataSourceCoroutines

    @Singleton
    @Binds
    fun bindRepositoryCoroutines(repository: PropertyRepositoryImplCoroutines):
        PropertyRepositoryCoroutines

    /*
        Coroutines + Pagination
     */

    @Singleton
    @Binds
    fun bindPagedLocalDataSource(localDataSource: LocalPagedPropertySourceImpl):
        LocalPagedPropertyDataSource

    @Singleton
    @Binds
    fun bindPagedRepository(repository: PagedPropertyRepositoryImpl):
        PagedPropertyRepository

    /*
        RxJava
     */
    @Singleton
    @Binds
    fun bindRemoteDataSourceRxJava3(remoteDataSource: RemoteDataSourceRxJava3Impl):
        RemotePropertyDataSourceRxJava3

    @Singleton
    @Binds
    fun bindLocalDataSourceRxJava3(localDataSource: LocalDataSourceRxJava3Impl):
        LocalPropertyDataSourceRxJava3

    @Singleton
    @Binds
    fun bindRepositoryRxJava3(repository: PropertyRepositoryImlRxJava3):
        PropertyRepositoryRxJava3
}

/**
 * This module is for injections with @Provides annotation
 */
@Module
@InstallIn(ApplicationComponent::class)
object DataProviderModule {
    @Provides
    fun provideDTOtoEntityMapper() = PropertyDTOtoEntityListMapper()
}
