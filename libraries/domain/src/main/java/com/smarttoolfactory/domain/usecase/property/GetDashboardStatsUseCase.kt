package com.smarttoolfactory.domain.usecase.property

import com.smarttoolfactory.data.model.remote.PropertyDTO
import com.smarttoolfactory.data.repository.FavoritesRepository
import com.smarttoolfactory.data.repository.PagedPropertyRepository
import com.smarttoolfactory.domain.ORDER_BY_PRICE_ASCENDING
import com.smarttoolfactory.domain.dispatcher.UseCaseDispatchers
import com.smarttoolfactory.domain.mapper.FavoriteEntityToItemListMapper
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
    private val mapperItemToEntity: PropertyItemToEntityMapper,
    private val mapperEntityToFavoriteItemMapper: FavoriteEntityToItemListMapper
) {

    /**
     * Get all of liked properties by the current user
     */
    fun getFavoriteProperties(userId: Long = 0): Flow<List<PropertyItem>> {

        return getPropertiesWithStats(userId)
            .map { propertyList ->
                propertyList.filter { property ->
                    property.isFavorite
                }
                    .take(5)
            }
    }

    /**
     * Get properties that at least once viewed by the current user
     */
    fun getMostViewedProperties(userId: Long = 0): Flow<List<PropertyItem>> {

        return getPropertiesWithStats(userId)
            .map { propertyList ->
                propertyList.filter { property ->
                    property.viewCount > 0
                }.sortedByDescending { property ->
                    property.viewCount
                }
                    .take(5)
            }
    }

    /**
     * Returns properties and and stats of these properties user with given id has interacted.
     *
     * Interaction with a property is either displaying it's details or setting
     * like status to true.
     *
     */
    private fun getPropertiesWithStats(userId: Long = 0): Flow<List<PropertyItem>> {

        return flow { emit(favoritesRepo.getPropertiesWithFavorites(userId)) }
            .map { propertiesWithFavorites ->
                mapperEntityToFavoriteItemMapper.map(propertiesWithFavorites)
            }
    }

    /**
     * Deals recommended for the current user based on previous like or visit history
     * * Deals recommendation are based on price range, number of bedrooms, bathrooms and property
     * type
     */
    fun getRecommendedDeals() {
        TODO()
    }

    /**
     * Combines 5 pages of Properties sorted by ascending price order and
     * searches for the deals in range between prices based on user's liked or visited items.
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
}
