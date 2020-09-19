package com.smarttoolfactory.data.repository

import com.smarttoolfactory.data.model.local.InteractivePropertyEntity
import com.smarttoolfactory.data.model.local.PropertyWithFavorites
import com.smarttoolfactory.data.model.local.UserFavoriteJunction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface FavoritesRepository {

    suspend fun insertOrUpdateFavorite(
        userId: Long,
        entity: InteractivePropertyEntity,
        viewCount: Int,
        liked: Boolean
    )

    /**
     * Get stats for ever property that at least has been interacted by displaying details
     * or setting as favorite
     */
    suspend fun getStatsForAll(): List<UserFavoriteJunction>

    /**
     * Retrieves favorite and view count stats for property with [propertyId] only for the user
     * with [userId]
     */
    suspend fun getPropertyStats(
        userId: Long,
        propertyId: Int
    ): UserFavoriteJunction?

    /**
     * Retrieves favorite and view count stats for every property for the user with [userId]
     */
    suspend fun getStatsForProperties(
        userId: Long
    ): List<UserFavoriteJunction>

    suspend fun getPropertiesWithFavorites(userId: Long): List<PropertyWithFavorites>

    suspend fun deleteFavoriteEntity(entity: InteractivePropertyEntity)
    suspend fun deleteFavoriteEntityForUser(userId: Long, propertyId: Int)
}

interface FavoritesRepositoryRxJava3 {

    fun insertOrUpdateFavorite(
        userId: Long,
        entity: InteractivePropertyEntity,
        viewCount: Int,
        liked: Boolean
    ): Single<Long>

    /**
     * Get stats for ever property that at least has been interacted by displaying details
     * or setting as favorite
     */
    fun getStatsForAll(): Single<List<UserFavoriteJunction>>

    /**
     * Retrieves favorite and view count stats for property with [propertyId] only for the user
     * with [userId]
     */
    fun getPropertyStats(
        userId: Long,
        propertyId: Int
    ): Single<UserFavoriteJunction?>

    /**
     * Retrieves favorite and view count stats for every property for the user with [userId]
     */
    fun getStatsForProperties(
        userId: Long
    ): Single<List<UserFavoriteJunction>>

    fun getPropertiesWithFavorites(userId: Long): Single<List<PropertyWithFavorites>>

    fun deleteFavoriteEntity(entity: InteractivePropertyEntity): Completable
    fun deleteFavoriteEntityForUser(userId: Long, propertyId: Int): Completable
}
