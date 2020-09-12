package com.smarttoolfactory.data.source

import com.smarttoolfactory.data.api.PropertyApiCoroutines
import com.smarttoolfactory.data.api.PropertyApiRxJava
import com.smarttoolfactory.data.db.dao.PagedPropertyDao
import com.smarttoolfactory.data.db.dao.PropertyDaoCoroutines
import com.smarttoolfactory.data.db.dao.PropertyDaoRxJava3
import com.smarttoolfactory.data.db.dao.SortOrderDaoCoroutines
import com.smarttoolfactory.data.db.dao.SortOrderDaoRxJava3
import com.smarttoolfactory.data.model.local.PagedPropertyEntity
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.local.SortOrderEntity
import com.smarttoolfactory.data.model.remote.PropertyDTO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/*
    *** Coroutines Implementations for DataSources ***
 */
class RemotePropertyDataSourceCoroutinesImpl
@Inject constructor(private val api: PropertyApiCoroutines) : RemotePropertyDataSourceCoroutines {

    override suspend fun getPropertyDTOs(orderBy: String): List<PropertyDTO> {
        return api.getPropertyResponse(orderBy).res
    }

    override suspend fun getPropertyDTOsWithPagination(
        page: Int,
        orderBy: String
    ): List<PropertyDTO> {
        return api.getPropertyResponseForPage(page, orderBy).res
    }
}

class LocalPropertyDataSourceImpl
@Inject constructor(
    private val dao: PropertyDaoCoroutines,
    private val sortDao: SortOrderDaoCoroutines
) : LocalPropertyDataSourceCoroutines {

    override suspend fun getPropertyEntities(): List<PropertyEntity> {
        return dao.getPropertyList()
    }

    override suspend fun saveEntities(properties: List<PropertyEntity>): List<Long> {
        return dao.insert(properties)
    }

    override suspend fun deletePropertyEntities() {
        return dao.deleteAll()
    }

    override suspend fun saveOrderKey(orderBy: String) {
        sortDao.insert(SortOrderEntity(orderBy = orderBy))
    }

    override suspend fun getOrderKey(): String {
        return sortDao.getSortOrderEntity()
    }
}

/*
    *** RxJava3 Implementations for DataSources ***
 */
class RemoteDataSourceRxJava3Impl @Inject constructor(private val api: PropertyApiRxJava) :
    RemotePropertyDataSourceRxJava3 {

    override fun getPropertyDTOs(orderBy: String): Single<List<PropertyDTO>> {
        return api.getPropertyResponse(orderBy).map { it.res }
    }

    override fun getPropertyDTOsWithPagination(
        page: Int,
        orderBy: String
    ): Single<List<PropertyDTO>> {
        return api.getPropertyResponseForPage(page, orderBy).map { it.res }
    }
}

class LocalDataSourceRxJava3Impl @Inject constructor(
    private val dao: PropertyDaoRxJava3,
    private val sortDao: SortOrderDaoRxJava3
) :
    LocalPropertyDataSourceRxJava3 {

    override fun getPropertyEntities(): Single<List<PropertyEntity>> {
        return dao.getPropertiesSingle()
    }

    override fun saveEntities(properties: List<PropertyEntity>): Completable {
        return dao.insert(properties)
    }

    override fun deletePropertyEntities(): Completable {
        return dao.deleteAll()
    }

    override fun saveOrderKey(orderBy: String): Completable {
        return sortDao.insert(SortOrderEntity(orderBy = orderBy))
    }

    override fun getOrderKey(): Single<String> {
        return sortDao.getSortOrderSingle()
    }
}

/*
    Paged Local Data Source
 */
class LocalPagedPropertySourceImpl @Inject constructor(
    private val dao: PagedPropertyDao,
    private val sortDao: SortOrderDaoCoroutines
) : LocalPagedPropertyDataSource {

    override suspend fun getPropertyEntities(): List<PagedPropertyEntity> {
        return dao.getPropertyList()
    }

    override suspend fun saveEntities(properties: List<PagedPropertyEntity>): List<Long> {
        return dao.insert(properties)
    }

    override suspend fun deletePropertyEntities() {
        return dao.deleteAll()
    }

    override suspend fun getPropertyCount(): Int {
        return dao.getPropertyCount()
    }

    override suspend fun saveOrderKey(orderBy: String) {
        sortDao.insert(SortOrderEntity(orderBy = orderBy))
    }

    override suspend fun getOrderKey(): String {
        return sortDao.getSortOrderEntity()
    }
}
