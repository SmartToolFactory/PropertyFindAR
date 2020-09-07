package com.smarttoolfactory.data.di

import android.app.Application
import androidx.room.Room
import com.smarttoolfactory.data.constant.DATABASE_NAME
import com.smarttoolfactory.data.db.PropertyDaoCoroutines
import com.smarttoolfactory.data.db.PropertyDaoRxJava3
import com.smarttoolfactory.data.db.PropertyDatabase
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
            .build()
    }

    @Singleton
    @Provides
    fun providePropertyDaoCoroutines(appDatabase: PropertyDatabase): PropertyDaoCoroutines =
        appDatabase.propertyDaoCoroutines()

    @Provides
    fun providePropertyDaoRxJava3(appDatabase: PropertyDatabase): PropertyDaoRxJava3 =
        appDatabase.propertyDaoRxJava()
}
