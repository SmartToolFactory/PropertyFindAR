package com.smarttoolfactory.data.repository

import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.mapper.PropertyDTOtoPagedEntityListMapper
import com.smarttoolfactory.data.model.local.PagedPropertyEntity
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.source.LocalPagedPropertyDataSource
import com.smarttoolfactory.data.source.LocalPropertyDataSourceCoroutines
import com.smarttoolfactory.data.source.LocalPropertyDataSourceRxJava3
import com.smarttoolfactory.data.source.RemotePropertyDataSourceCoroutines
import com.smarttoolfactory.data.source.RemotePropertyDataSourceRxJava3
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PropertyRepositoryImplCoroutines @Inject constructor(
    private val localDataSource: LocalPropertyDataSourceCoroutines,
    private val remoteDataSource: RemotePropertyDataSourceCoroutines,
    private val mapper: PropertyDTOtoEntityListMapper
) : PropertyRepositoryCoroutines {

    override suspend fun fetchEntitiesFromRemote(orderBy: String): List<PropertyEntity> {
        saveSortOrderKey(orderBy)
        return mapper.map(remoteDataSource.getPropertyDTOs(orderBy))
    }

    override suspend fun getPropertyEntitiesFromLocal(): List<PropertyEntity> {
        return localDataSource.getPropertyEntities()
    }

    override suspend fun savePropertyEntities(propertyEntities: List<PropertyEntity>) {
        localDataSource.saveEntities(propertyEntities)
    }

    override suspend fun deletePropertyEntities() {
        localDataSource.deletePropertyEntities()
    }

    override suspend fun saveSortOrderKey(orderBy: String) {
        localDataSource.saveOrderKey(orderBy)
    }

    override suspend fun getSortOrderKey(): String {
        return localDataSource.getOrderKey()
    }
}

class PropertyRepositoryImlRxJava3 @Inject constructor(
    private val localDataSource: LocalPropertyDataSourceRxJava3,
    private val remoteDataSource: RemotePropertyDataSourceRxJava3,
    private val mapper: PropertyDTOtoEntityListMapper
) : PropertyRepositoryRxJava3 {

    override fun fetchEntitiesFromRemote(orderBy: String): Single<List<PropertyEntity>> {

        return remoteDataSource.getPropertyDTOs(orderBy)
            .map {
                saveSortOrderKey(orderBy)
                mapper.map(it)
            }
    }

    override fun getPropertyEntitiesFromLocal(): Single<List<PropertyEntity>> {
        return localDataSource.getPropertyEntities()
    }

    override fun savePropertyEntities(propertyEntities: List<PropertyEntity>): Completable {
        return localDataSource.saveEntities(propertyEntities)
    }

    override fun deletePropertyEntities(): Completable {
        return localDataSource.deletePropertyEntities()
    }

    override fun saveSortOrderKey(orderBy: String): Completable {
        return localDataSource.saveOrderKey(orderBy)
    }

    override fun getSortOrderKey(): Single<String> {
        return localDataSource.getOrderKey()
    }
}

class PagedPropertyRepositoryImpl @Inject constructor(
    private val localDataSource: LocalPagedPropertyDataSource,
    private val remoteDataSource: RemotePropertyDataSourceCoroutines,
    private val mapper: PropertyDTOtoPagedEntityListMapper
) : PagedPropertyRepository {

    private var currentPageNumber = 0

    override fun getCurrentPageNumber(): Int {
        return currentPageNumber
    }

    override suspend fun fetchEntitiesFromRemoteByPage(
        page: Int,
        orderBy: String
    ): List<PagedPropertyEntity> {
        currentPageNumber = page
        saveSortOrderKey(orderBy)
        return mapper.map(remoteDataSource.getPropertyDTOsWithPagination(page, orderBy))
    }

    override suspend fun getPropertyEntitiesFromLocal(): List<PagedPropertyEntity> {
        return localDataSource.getPropertyEntities()
    }

    override suspend fun savePropertyEntities(propertyEntities: List<PagedPropertyEntity>) {
        localDataSource.saveEntities(propertyEntities)
    }

    override suspend fun deletePropertyEntities() {
        localDataSource.deletePropertyEntities()
    }

    override suspend fun saveSortOrderKey(orderBy: String) {
        localDataSource.saveOrderKey(orderBy)
    }

    override suspend fun getSortOrderKey(): String {
        return localDataSource.getOrderKey()
    }
}
