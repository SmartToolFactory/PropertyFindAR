package com.smarttoolfactory.data.repository

import com.smarttoolfactory.data.model.local.InteractivePropertyEntity
import com.smarttoolfactory.data.model.local.PropertyWithFavorites
import com.smarttoolfactory.data.model.local.UserFavoriteJunction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface FavoritesRepositoryCoroutines {

    suspend fun insertOrUpdateFavorite(
        userId: Long,
        entity: InteractivePropertyEntity,
        viewCount: Int,
        liked: Boolean
    )

    suspend fun getUserFavoriteJunction(
        userId: Long,
        propertyId: Int
    ): UserFavoriteJunction?

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

    fun getUserFavoriteJunction(
        userId: Long,
        propertyId: Int
    ): Single<UserFavoriteJunction?>

    fun getPropertiesWithFavorites(userId: Long): Single<List<PropertyWithFavorites>>

    fun deleteFavoriteEntity(entity: InteractivePropertyEntity): Completable
    fun deleteFavoriteEntityForUser(userId: Long, propertyId: Int): Completable
}
