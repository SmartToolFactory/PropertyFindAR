package com.smarttoolfactory.domain.usecase.property

import com.smarttoolfactory.data.model.local.UserFavoriteJunction
import com.smarttoolfactory.data.repository.FavoritesRepositoryRxJava3
import com.smarttoolfactory.domain.mapper.PropertyItemToEntityMapper
import com.smarttoolfactory.domain.model.PropertyItem
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SetPropertyStatsUseCaseRxJava3 @Inject constructor(
    private val favoritesRepo: FavoritesRepositoryRxJava3,
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
    ): Single<List<PropertyItem>> {
        return favoritesRepo.getStatsForProperties(userId)
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

    fun updatePropertyStatus(
        userId: Long = 0,
        property: PropertyItem
    ): Single<Long> {

        return Single.just(mapper.map(property))
            .flatMap { propertyEntity ->

                if (!property.isFavorite && property.viewCount == 0) {
                    favoritesRepo.deleteFavoriteEntity(propertyEntity).andThen(Single.just(-1L))
                } else {
                    favoritesRepo.insertOrUpdateFavorite(
                        userId,
                        propertyEntity,
                        property.viewCount,
                        property.isFavorite
                    )
                }
            }
    }
}
