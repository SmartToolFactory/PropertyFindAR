package com.smarttoolfactory.domain.usecase

import com.smarttoolfactory.data.constant.ORDER_BY_NONE
import com.smarttoolfactory.data.model.local.PagedPropertyEntity
import com.smarttoolfactory.data.repository.PagedPropertyRepository
import com.smarttoolfactory.domain.dispatcher.UseCaseDispatchers
import com.smarttoolfactory.domain.error.EmptyDataException
import com.smarttoolfactory.domain.mapper.PagedEntityToItemListMapper
import com.smarttoolfactory.domain.model.PropertyItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetPropertiesUseCasePaged @Inject constructor(
    private val repository: PagedPropertyRepository,
    private val mapper: PagedEntityToItemListMapper,
    private val dispatcherProvider: UseCaseDispatchers
) {

    /**
     * Fetches data from REMOTE source with PAGINATION
     * using specified parameter parameter. Page number i s kept in [PagedPropertyRepository].
     *
     * * Since [PagedPropertyEntity] insert order is not kept
     * it's required to add an index for insertion or to db to get valid data which is ordered
     * in server side
     */
    fun getPagedOfflineLast(orderBy: String): Flow<List<PropertyItem>> {

        return flow { emit(repository.fetchEntitiesFromRemoteByPage(orderBy)) }
            .map {
                if (it.isNullOrEmpty()) {
                    throw EmptyDataException("No Data is available in Remote source!")
                } else {
                    repository.run {

//                        deletePropertyEntities()

                        val propertyCount = repository.getPropertyCount()

                        // 🔥 Add an insert order since we are not using Room's ORDER BY
                        it.forEachIndexed { index, propertyEntity ->
                            propertyEntity.insertOrder = propertyCount + index
                        }
                        savePropertyEntities(it)
                        getPropertyEntitiesFromLocal()
                    }
                }
            }
            .flowOn(dispatcherProvider.ioDispatcher)
            // This is where remote exception or least likely db exceptions are caught
            .catch { throwable ->
                emitAll(flowOf(repository.getPropertyEntitiesFromLocal()))
            }
            .map {
                toPropertyListOrError(it)
            }
    }

    /**
     * Resets page number to 1, clears previous data from database and fetches new data
     * from REMOTE source with given param
     */
    fun refreshData(orderBy: String): Flow<List<PropertyItem>> {
        return flow { emit(repository.resetPageCount()) }
            .flatMapConcat {
                repository.deletePropertyEntities()
                getPagedOfflineLast(orderBy)
            }
    }

    private fun toPropertyListOrError(entityList: List<PagedPropertyEntity>): List<PropertyItem> {
        return if (!entityList.isNullOrEmpty()) {
            mapper.map(entityList)
        } else {
            throw EmptyDataException("Empty data mapping error!")
        }
    }

    /**
     * Get current sort key from db
     */
    fun getCurrentSortKey(defaultKey: String = ORDER_BY_NONE): Flow<String?> {
        return flow { emit(repository.getSortOrderKey()) }
            .catch {
                emitAll(flowOf(defaultKey))
            }
    }
}
