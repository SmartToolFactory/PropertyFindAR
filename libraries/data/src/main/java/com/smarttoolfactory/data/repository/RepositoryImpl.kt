package com.smarttoolfactory.data.repository

import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.source.LocalPropertyDataSourceCoroutines
import com.smarttoolfactory.data.source.LocalPropertyDataSourceRxJava3
import com.smarttoolfactory.data.source.RemotePropertyDataSourceCoroutines
import com.smarttoolfactory.data.source.RemotePropertyDataSourceRxJava3
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class PropertyRepositoryImplCoroutines(
    private val localDataSource: LocalPropertyDataSourceCoroutines,
    private val remoteDataSource: RemotePropertyDataSourceCoroutines,
    private val mapper: PropertyDTOtoEntityListMapper
) : PropertyRepositoryCoroutines {

    override fun getCurrentPageNumber(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun fetchEntitiesFromRemote(orderBy: String): List<PropertyEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchEntitiesFromRemoteByPage(
        page: Int,
        orderBy: String
    ): List<PropertyEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getPropertyEntitiesFromLocal(): List<PropertyEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun savePropertyEntities(propertyEntities: List<PropertyEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePropertyEntities() {
        TODO("Not yet implemented")
    }
}

class PropertyRepositoryImlRxJava3(
    private val localDataSource: LocalPropertyDataSourceRxJava3,
    private val remoteDataSource: RemotePropertyDataSourceRxJava3,
    private val mapper: PropertyDTOtoEntityListMapper
) : PropertyRepositoryRxJava3 {

    override fun getCurrentPageNumber(): Int {
        TODO("Not yet implemented")
    }

    override fun fetchEntitiesFromRemote(orderBy: String): Single<List<PropertyEntity>> {
        TODO("Not yet implemented")
    }

    override fun fetchEntitiesFromRemoteByPage(
        page: Int,
        orderBy: String
    ): Single<List<PropertyEntity>> {
        TODO("Not yet implemented")
    }

    override fun getPropertyEntitiesFromLocal(): Single<List<PropertyEntity>> {
        TODO("Not yet implemented")
    }

    override fun savePropertyEntities(propertyEntities: List<PropertyEntity>): Completable {
        TODO("Not yet implemented")
    }

    override fun deletePropertyEntities(): Completable {
        TODO("Not yet implemented")
    }
}
