package com.smarttoolfactory.core.di

import com.smarttoolfactory.data.di.DatabaseModule
import com.smarttoolfactory.data.di.NetworkModule
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.repository.FavoritesRepository
import com.smarttoolfactory.data.repository.FavoritesRepositoryImpl
import com.smarttoolfactory.data.repository.FavoritesRepositoryImplRxJava3
import com.smarttoolfactory.data.repository.FavoritesRepositoryRxJava3
import com.smarttoolfactory.data.repository.PagedPropertyRepository
import com.smarttoolfactory.data.repository.PagedPropertyRepositoryImpl
import com.smarttoolfactory.data.repository.PropertyRepository
import com.smarttoolfactory.data.repository.PropertyRepositoryImlRxJava3
import com.smarttoolfactory.data.repository.PropertyRepositoryImpl
import com.smarttoolfactory.data.repository.PropertyRepositoryRxJava3
import com.smarttoolfactory.data.source.FavoritePropertyDataSource
import com.smarttoolfactory.data.source.FavoritePropertyDataSourceImpl
import com.smarttoolfactory.data.source.FavoritePropertyDataSourceImplRxJava3
import com.smarttoolfactory.data.source.FavoritePropertyDataSourceRxJava3
import com.smarttoolfactory.data.source.LocalDataSourceImplRxJava3
import com.smarttoolfactory.data.source.LocalPagedPropertyDataSource
import com.smarttoolfactory.data.source.LocalPagedPropertySourceImpl
import com.smarttoolfactory.data.source.LocalPropertyDataSource
import com.smarttoolfactory.data.source.LocalPropertyDataSourceImpl
import com.smarttoolfactory.data.source.LocalPropertyDataSourceRxJava3
import com.smarttoolfactory.data.source.RemoteDataSourceImplRxJava3
import com.smarttoolfactory.data.source.RemotePropertyDataSource
import com.smarttoolfactory.data.source.RemotePropertyDataSourceImpl
import com.smarttoolfactory.data.source.RemotePropertyDataSourceRxJava3
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module(
    includes = [
        DataProviderModule::class,
        DataModuleRxJava3::class,
        NetworkModule::class,
        DatabaseModule::class
    ]
)
interface DataModule {

    /*
        Coroutines
     */
    @Singleton
    @Binds
    fun bindRemoteDataSource(remoteDataSource: RemotePropertyDataSourceImpl):
        RemotePropertyDataSource

    @Singleton
    @Binds
    fun bindLocalDataSource(localDataSource: LocalPropertyDataSourceImpl):
        LocalPropertyDataSource

    @Singleton
    @Binds
    fun bindRepository(repository: PropertyRepositoryImpl):
        PropertyRepository

    @Singleton
    @Binds
    fun bindPagedLocalDataSource(localDataSource: LocalPagedPropertySourceImpl):
        LocalPagedPropertyDataSource

    @Singleton
    @Binds
    fun bindFavoriteDataSource(
        favoriteDataSource: FavoritePropertyDataSourceImpl
    ): FavoritePropertyDataSource

    @Singleton
    @Binds
    fun bindPagedRepository(repository: PagedPropertyRepositoryImpl):
        PagedPropertyRepository

    @Singleton
    @Binds
    fun bindFavoritesRepoC(repository: FavoritesRepositoryImpl):
        FavoritesRepository
}

@Module
@InstallIn(ApplicationComponent::class)
interface DataModuleRxJava3 {

    @Singleton
    @Binds
    fun bindRemoteDataSourceRxJava3(remoteDataSource: RemoteDataSourceImplRxJava3):
        RemotePropertyDataSourceRxJava3

    @Singleton
    @Binds
    fun bindLocalDataSourceRxJava3(localDataSource: LocalDataSourceImplRxJava3):
        LocalPropertyDataSourceRxJava3

    @Singleton
    @Binds
    fun bindFavoriteDataSourceRxJava3(favoriteDataSource: FavoritePropertyDataSourceImplRxJava3):
        FavoritePropertyDataSourceRxJava3

    @Singleton
    @Binds
    fun bindRepositoryRxJava3(repository: PropertyRepositoryImlRxJava3):
        PropertyRepositoryRxJava3

    @Singleton
    @Binds
    fun bindFavoritesReposRxJava3(localDataSource: FavoritesRepositoryImplRxJava3):
        FavoritesRepositoryRxJava3
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
