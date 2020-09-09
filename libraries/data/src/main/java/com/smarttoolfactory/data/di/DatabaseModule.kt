package com.smarttoolfactory.data.di

import android.app.Application
import androidx.room.Room
import com.smarttoolfactory.data.constant.DATABASE_NAME
import com.smarttoolfactory.data.db.MIGRATION_1_2
import com.smarttoolfactory.data.db.MIGRATION_2_3
import com.smarttoolfactory.data.db.PagedPropertyDao
import com.smarttoolfactory.data.db.PropertyDaoCoroutines
import com.smarttoolfactory.data.db.PropertyDaoRxJava3
import com.smarttoolfactory.data.db.PropertyDatabase
import com.smarttoolfactory.data.db.SortOrderDaoCoroutines
import com.smarttoolfactory.data.db.SortOrderDaoRxJava3
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): PropertyDatabase {
        return Room.databaseBuilder(
            application,
            PropertyDatabase::class.java,
            DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    @Singleton
    @Provides
    fun providePropertyDaoCoroutines(appDatabase: PropertyDatabase): PropertyDaoCoroutines =
        appDatabase.propertyDaoCoroutines()

    @Singleton
    @Provides
    fun providePropertyDaoRxJava3(appDatabase: PropertyDatabase): PropertyDaoRxJava3 =
        appDatabase.propertyDaoRxJava()

    @Singleton
    @Provides
    fun providePropertySortDaoCoroutines(appDatabase: PropertyDatabase): SortOrderDaoCoroutines =
        appDatabase.propertySortDaoCoroutines()

    @Singleton
    @Provides
    fun provideSortOrderDaoRxJava3(appDatabase: PropertyDatabase): SortOrderDaoRxJava3 =
        appDatabase.propertySortDaoRxJava()

    @Singleton
    @Provides
    fun providePagedPropertyDao(appDatabase: PropertyDatabase): PagedPropertyDao =
        appDatabase.pagedPropertyDao()
}
