package com.smarttoolfactory.domain.usecase

import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.repository.PropertyRepositoryRxJava3
import com.smarttoolfactory.domain.error.EmptyDataException
import com.smarttoolfactory.domain.mapper.PropertyEntityToItemListMapper
import com.smarttoolfactory.domain.model.PropertyItem
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * UseCase for getting UI Item list with offline first or offline last approach.
 *
 * * *Offline-first* first source to look for data is local data source, or database,
 * if database is empty or if caching is used it's expiry date is over, looks for remote source
 * for data. If both of the sources are empty, either return empty list or error to notify UI.
 *
 * This approach is good for when user is offline or have no internet connection, additional logic
 * can be added to check if user is offline to not deleted cached data.
 *
 * * *Offline-last* always checks remote data source for data and applies to database or offline
 * source as last resort. Offline-last is used especially when user refreshes data with a UI
 * element to get the latest data or new data is always first preference.
 */
class GetPropertiesUseCaseRxJava3 @Inject constructor(
    private val repository: PropertyRepositoryRxJava3,
    private val mapper: PropertyEntityToItemListMapper,
) {

    /**
     * Function to retrieve data from repository with offline-last which checks
     * REMOTE data source first.
     *
     * * Check out Remote Source first
     * * If empty data or null returned throw empty set exception
     * * If error occurred while fetching data from remote: Try to fetch data from db
     * * If data is fetched from remote source: delete old data, save new data and return new data
     * * If both network and db don't have any data throw empty set exception
     *
     */
    fun getPropertiesOfflineLast(orderBy: String): Single<List<PropertyItem>> {

        return repository.fetchEntitiesFromRemote(orderBy)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMap {
                if (it.isNullOrEmpty()) {
                    throw EmptyDataException("No Data is available in remote source!")
                } else {
                    repository.deletePropertyEntities()
                        .andThen(repository.savePropertyEntities(it))
                        .andThen(repository.getPropertyEntitiesFromLocal())
                }
            }
            .onErrorResumeNext { throwable ->
                repository.getPropertyEntitiesFromLocal()
            }
            .toPropertyListOrError()
    }

    /**
     * Function to retrieve data from repository with offline-first which checks
     * LOCAL data source first.
     *
     * * Check out Local Source first
     * * If empty data or null returned throw empty set exception
     * * If error occurred while fetching data from remote: Try to fetch data from db
     * * If data is fetched from remote source: delete old data, save new data and return new data
     * * If both network and db don't have any data throw empty set exception
     *
     */
    fun getPropertiesOfflineFirst(orderBy: String): Single<List<PropertyItem>> {

        return if (repository.getOrderFilter() != orderBy) {
            getPropertiesOfflineLast(orderBy)
        } else {
            return repository.getPropertyEntitiesFromLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .onErrorResumeNext { cause ->
                    Single.just(listOf())
                }
                .concatMap { list ->
                    if (list.isNullOrEmpty()) {
                        repository.fetchEntitiesFromRemote()
                            .concatMap {
                                repository.deletePropertyEntities()
                                    .andThen(repository.savePropertyEntities(it))
                                    .andThen(Single.just(it))
                            }
                    } else {
                        Single.just(list)
                    }
                }
                .onErrorResumeNext { cause ->
                    Single.just(listOf())
                }
                .toPropertyListOrError()
        }
    }

    /**
     * Uses `if(propertyListEntity.isNotEmpty) mapper.map(it)  else Single.error`
     * with RxJava to map [PropertyEntity] list to [PropertyItem]
     */
    private fun Single<List<PropertyEntity>>.toPropertyListOrError(): Single<List<PropertyItem>> {
        return this
            .filter { it.isNotEmpty() }
            .map { mapper.map(it) }
            .switchIfEmpty(
                Single.error(EmptyDataException("Data is not available in any source!"))
            )
    }
}
