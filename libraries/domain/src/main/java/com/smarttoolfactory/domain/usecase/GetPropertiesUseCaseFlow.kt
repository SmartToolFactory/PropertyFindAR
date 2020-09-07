package com.smarttoolfactory.domain.usecase

import com.smarttoolfactory.data.repository.PropertyRepositoryCoroutines
import com.smarttoolfactory.domain.dispatcher.UseCaseDispatchers
import com.smarttoolfactory.domain.mapper.PropertyEntityToItemListMapper
import com.smarttoolfactory.domain.model.PropertyItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

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
class GetPropertiesUseCaseFlow @Inject constructor(
    private val repository: PropertyRepositoryCoroutines,
    private val entityToItemMapper: PropertyEntityToItemListMapper,
    private val dispatcherProvider: UseCaseDispatchers
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
    fun getPropertiesOfflineLast(orderBy: String): Flow<List<PropertyItem>> {
        TODO()
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
    fun getPropertiesOfflineFirst(orderBy: String): Flow<List<PropertyItem>> {
        TODO()
    }
}
