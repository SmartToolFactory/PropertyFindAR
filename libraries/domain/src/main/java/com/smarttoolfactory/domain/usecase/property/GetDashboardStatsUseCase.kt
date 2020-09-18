package com.smarttoolfactory.domain.usecase.property

import com.smarttoolfactory.data.model.local.UserFavoriteJunction
import com.smarttoolfactory.data.model.remote.PropertyDTO
import com.smarttoolfactory.data.repository.FavoritesRepository
import com.smarttoolfactory.data.repository.PagedPropertyRepository
import com.smarttoolfactory.domain.ORDER_BY_PRICE_ASCENDING
import com.smarttoolfactory.domain.dispatcher.UseCaseDispatchers
import com.smarttoolfactory.domain.mapper.PropertyDTOtoItemListMapper
import com.smarttoolfactory.domain.mapper.PropertyItemToEntityMapper
import com.smarttoolfactory.domain.model.PropertyItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetDashboardStatsUseCase @Inject constructor(
    private val propertyRepo: PagedPropertyRepository,
    private val mapperDTOtoItem: PropertyDTOtoItemListMapper,
    private val dispatcherProvider: UseCaseDispatchers,
    private val favoritesRepo: FavoritesRepository,
    private val mapperItemToEntity: PropertyItemToEntityMapper
) {

    /**
     * Combines 5 pages of Properties sorted by ascending price order and
     * searches for the deals in range between []
     */
    private fun getSumOfFivePages(): Flow<List<PropertyItem>> {

        val listOfFlows = mutableListOf<Flow<List<PropertyDTO>>>()

        for (i in 1..5) {

            listOfFlows.add(
                flow {
                    for (i in 1..5) {
                        emit(
                            propertyRepo.fetchEntitiesFromRemoteByPage(
                                i,
                                ORDER_BY_PRICE_ASCENDING
                            )
                        )
                    }
                }
            )
        }

        return combine(*listOfFlows.toTypedArray()) { array ->

            val mutableList = mutableListOf<PropertyDTO>()
            array.forEach {
                mutableList + it
            }

            mutableList
        }
            .map {
                mapperDTOtoItem.map(it)
            }
    }

    /**
     * Get all of liked properties by the current user
     */
    fun getLikedProperties(
        userId: Long = 0,
        properties: List<PropertyItem>
    ): Flow<List<PropertyItem>> {

        return getStatusOfPropertiesForUser(userId, properties)
            .map {
                it.filter { property ->
                    property.isFavorite
                }
            }
    }

    /**
     * Get properties that at least once viewed by the current user
     */
    fun getMostViewedProperties(
        userId: Long = 0,
        properties: List<PropertyItem>
    ): Flow<List<PropertyItem>> {

        return getStatusOfPropertiesForUser(userId, properties)
            .map {
                it.filter { property ->
                    property.viewCount > 0
                }.sortedByDescending { property ->
                    property.viewCount
                }
            }
    }

    /**
     * Get status of properties user with given id has interacted.
     *
     * Interaction with a property is either displaying it's details or setting
     * like status to true.
     *
     */
    private fun getStatusOfPropertiesForUser(
        userId: Long = 0,
        properties: List<PropertyItem>
    ): Flow<List<PropertyItem>> {
        return flow { emit(favoritesRepo.getStatsForProperties(userId)) }
            .map { stats ->
                if (stats.isNullOrEmpty()) {
                    properties
                } else {
                    modifyPropertyStatus(properties, stats)
                }
            }
    }

    /**
     * Search method for match [PropertyItem] with [PropertyItem.id] to [UserFavoriteJunction]
     * with [UserFavoriteJunction.propertyId] to set like and view count of any possible
     * [PropertyItem] in given list.
     *
     * ### Note: This is LINEAR SEARCH, can be converted to BINARY or other search algorithms
     */
    private fun modifyPropertyStatus(
        properties: List<PropertyItem>,
        stats: List<UserFavoriteJunction>
    ): List<PropertyItem> {

        return properties.map { propertyItem ->

            stats.map { stat ->

                if (stat.propertyId == propertyItem.id) {
                    propertyItem.viewCount = stat.viewCount
                    propertyItem.isFavorite = stat.liked
                }
            }

            propertyItem
        }
    }
}
