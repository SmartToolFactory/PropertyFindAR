package com.smarttoolfactory.data.repository

import com.smarttoolfactory.data.constant.ORDER_BY_NONE
import com.smarttoolfactory.data.model.local.PagedPropertyEntity
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.remote.PropertyDTO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * This repository contains no data save, delete or fetch logic with Coroutines.
 *
 * All business logic for creating offline-first or offline-last approach is moved to UseCase
 */
interface PropertyRepository {

    suspend fun fetchEntitiesFromRemote(orderBy: String = ORDER_BY_NONE): List<PropertyEntity>

    suspend fun getPropertyEntitiesFromLocal(): List<PropertyEntity>

    suspend fun savePropertyEntities(propertyEntities: List<PropertyEntity>)

    suspend fun deletePropertyEntities()

    suspend fun saveSortOrderKey(orderBy: String)
    suspend fun getSortOrderKey(): String
}

/**
 * This repository contains no data save, delete or fetch logic with RxJava3.
 *
 * All business logic for creating offline-first or offline-last approach is moved to UseCase
 */
interface PropertyRepositoryRxJava3 {

    fun fetchEntitiesFromRemote(orderBy: String = ORDER_BY_NONE): Single<List<PropertyEntity>>

    fun getPropertyEntitiesFromLocal(): Single<List<PropertyEntity>>

    fun savePropertyEntities(propertyEntities: List<PropertyEntity>): Completable

    fun deletePropertyEntities(): Completable

    fun saveSortOrderKey(orderBy: String): Completable
    fun getSortOrderKey(): Single<String>
}

interface PagedPropertyRepository {

    fun getCurrentPageNumber(): Int

    /**
     * Fetch entities by remote using pagination in Repository and increasing page number
     * with each request to fetch next page of result.
     */
    suspend fun fetchPagedEntitiesFromRemote(
        orderBy: String = ORDER_BY_NONE
    ): List<PagedPropertyEntity>

    /**
     * Returns network result without mapping to any DB entity.
     */
    suspend fun fetchEntitiesFromRemoteByPage(
        page: Int,
        orderBy: String = ORDER_BY_NONE,
    ): List<PropertyDTO>

    suspend fun getPropertyCount(): Int

    fun resetPageCount()

    suspend fun getPropertyEntitiesFromLocal(): List<PagedPropertyEntity>

    suspend fun savePropertyEntities(propertyEntities: List<PagedPropertyEntity>)

    suspend fun deletePropertyEntities()

    suspend fun saveSortOrderKey(orderBy: String)
    suspend fun getSortOrderKey(): String
}
