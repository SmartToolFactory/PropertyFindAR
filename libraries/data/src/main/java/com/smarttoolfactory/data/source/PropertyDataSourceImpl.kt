package com.smarttoolfactory.data.source

import com.smarttoolfactory.data.api.PropertyApiCoroutines
import com.smarttoolfactory.data.api.PropertyApiRxJava
import com.smarttoolfactory.data.db.PropertyDaoCoroutines
import com.smarttoolfactory.data.db.PropertyDaoRxJava3
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.remote.PropertyDTO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/*
    *** Coroutines Implementations for PostDataSources ***
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
@Inject constructor(private val dao: PropertyDaoCoroutines) : LocalPropertyDataSourceCoroutines {

    override suspend fun getPropertyEntities(): List<PropertyEntity> {
        return dao.getPropertyList()
    }

    override suspend fun saveEntities(properties: List<PropertyEntity>): List<Long> {
        return dao.insert(properties)
    }

    override suspend fun deletePropertyEntities() {
        return dao.deleteAll()
    }
}

/*
    *** RxJava3 Implementations for PostDataSources ***
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

class LocalDataSourceRxJava3Impl @Inject constructor(private val dao: PropertyDaoRxJava3) :
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
}
