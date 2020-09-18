package com.smarttoolfactory.data.repository

import com.smarttoolfactory.data.model.local.InteractivePropertyEntity
import com.smarttoolfactory.data.model.local.PropertyWithFavorites
import com.smarttoolfactory.data.model.local.UserFavoriteJunction
import com.smarttoolfactory.data.source.FavoritePropertyDataSource
import com.smarttoolfactory.data.source.FavoritePropertyDataSourceRxJava3
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val favoriteSource: FavoritePropertyDataSource
) : FavoritesRepository {

    override suspend fun insertOrUpdateFavorite(
        userId: Long,
        entity: InteractivePropertyEntity,
        viewCount: Int,
        liked: Boolean
    ) {
        favoriteSource.insertOrUpdateFavorite(userId, entity, viewCount, liked)
    }

    override suspend fun getStatsForAll(): List<UserFavoriteJunction> {
        return favoriteSource.getStatsForAll()
    }

    override suspend fun getPropertyStats(
        userId: Long,
        propertyId: Int
    ): UserFavoriteJunction? {
        return favoriteSource.getPropertyStats(userId, propertyId)
    }

    override suspend fun getStatsForProperties(userId: Long): List<UserFavoriteJunction> {
        return favoriteSource.getStatsForProperties(userId)
    }

    override suspend fun getPropertiesWithFavorites(userId: Long): List<PropertyWithFavorites> {
        return favoriteSource.getPropertiesWithFavorites(userId = userId)
    }

    override suspend fun deleteFavoriteEntity(entity: InteractivePropertyEntity) {
        favoriteSource.deleteFavoriteEntity(entity)
    }

    override suspend fun deleteFavoriteEntityForUser(userId: Long, propertyId: Int) {
        favoriteSource.deleteFavoriteEntityForUser(userId, propertyId)
    }
}

class FavoritesRepositoryImplRxJava3 @Inject constructor(
    private val favoriteSource: FavoritePropertyDataSourceRxJava3
) : FavoritesRepositoryRxJava3 {

    override fun insertOrUpdateFavorite(
        userId: Long,
        entity: InteractivePropertyEntity,
        viewCount: Int,
        liked: Boolean
    ): Single<Long> {
        return favoriteSource.insertOrUpdateFavorite(userId, entity, viewCount, liked)
    }

    override fun getStatsForAll(): Single<List<UserFavoriteJunction>> {
        return favoriteSource.getStatsForAll()
    }

    override fun getPropertyStats(
        userId: Long,
        propertyId: Int
    ): Single<UserFavoriteJunction?> {
        return favoriteSource.getPropertyStats(userId, propertyId)
    }

    override fun getStatsForProperties(userId: Long): Single<List<UserFavoriteJunction>> {
        return favoriteSource.getPropertyStatsList(userId)
    }

    override fun getPropertiesWithFavorites(userId: Long): Single<List<PropertyWithFavorites>> {
        return favoriteSource.getPropertiesWithFavorites(userId = userId)
    }

    override fun deleteFavoriteEntity(entity: InteractivePropertyEntity): Completable {
        return favoriteSource.deleteFavoriteEntity(entity)
    }

    override fun deleteFavoriteEntityForUser(userId: Long, propertyId: Int): Completable {
        return favoriteSource.deleteFavoriteEntityForUser(userId, propertyId)
    }
}
