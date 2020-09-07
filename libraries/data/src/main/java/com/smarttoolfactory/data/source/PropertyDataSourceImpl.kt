package com.smarttoolfactory.data.source

import com.smarttoolfactory.data.api.PropertyApiCoroutines
import com.smarttoolfactory.data.db.PropertyDaoCoroutines
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.remote.PropertyDTO
import javax.inject.Inject

/*
    *** Coroutines Implementations for PostDataSources ***
 */
class RemotePropertyDataSourceCoroutinesImpl
@Inject constructor(private val api: PropertyApiCoroutines) : RemotePropertyDataSourceCoroutines {

    override suspend fun getPropertyDTOs(orderBy: String): List<PropertyDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getPropertyDTOsWithPagination(
        page: Int,
        orderBy: String
    ): List<PropertyDTO> {
        TODO("Not yet implemented")
    }
}

class LocalPropertyDataSourceImpl
@Inject constructor(private val dao: PropertyDaoCoroutines) : LocalPropertyDataSourceCoroutines {

    override suspend fun getPropertyEntities(): List<PropertyEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun saveEntities(properties: List<PropertyEntity>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePropertyEntities() {
        TODO("Not yet implemented")
    }
}
