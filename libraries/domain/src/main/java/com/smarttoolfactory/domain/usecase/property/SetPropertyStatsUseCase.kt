package com.smarttoolfactory.domain.usecase.property

import com.smarttoolfactory.data.model.local.UserFavoriteJunction
import com.smarttoolfactory.data.repository.FavoritesRepository
import com.smarttoolfactory.domain.mapper.PropertyItemToEntityMapper
import com.smarttoolfactory.domain.model.PropertyItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SetPropertyStatsUseCase @Inject constructor(
    private val favoritesRepo: FavoritesRepository,
    private val mapper: PropertyItemToEntityMapper
) {

    /**
     * Get status of properties user with given id has interacted.
     *
     * Interaction with a property is either displaying it's details or setting like status
     */
    fun getStatusOfPropertiesForUser(
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

    // TODO Add delete function for junction table for property that is not
    //  liked nor visited by any users
    fun updatePropertyStatus(
        userId: Long = 0,
        property: PropertyItem
    ): Flow<Unit> {

        return flow { emit(mapper.map(property)) }
            .map { propertyEntity ->

                favoritesRepo.insertOrUpdateFavorite(
                    userId,
                    propertyEntity,
                    property.viewCount,
                    property.isFavorite
                )
            }
    }
}
