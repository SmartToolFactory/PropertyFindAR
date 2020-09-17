package com.smarttoolfactory.data.source

import com.smarttoolfactory.data.db.dao.FavoritesRxJava3Dao
import com.smarttoolfactory.data.model.local.InteractivePropertyEntity
import com.smarttoolfactory.data.model.local.PropertyWithFavorites
import com.smarttoolfactory.data.model.local.UserFavoriteJunction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface FavoritePropertyDataSourceRxJava3 {

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

class FavoritePropertyDataSourceImplRxJava3 @Inject constructor(
    private val favoritesDao: FavoritesRxJava3Dao
) : FavoritePropertyDataSourceRxJava3 {

    override fun insertOrUpdateFavorite(
        userId: Long,
        entity: InteractivePropertyEntity,
        viewCount: Int,
        liked: Boolean
    ): Single<Long> {
        return favoritesDao.insertUserFavorite(userId, entity, viewCount, liked)
    }

    override fun getUserFavoriteJunction(
        userId: Long,
        propertyId: Int
    ): Single<UserFavoriteJunction?> {
        return favoritesDao.getUserFavoriteJunction(userId, propertyId)
    }

    override fun getPropertiesWithFavorites(userId: Long): Single<List<PropertyWithFavorites>> {
        return favoritesDao.getPropertiesWithFavorites(userId = userId)
    }

    override fun deleteFavoriteEntity(entity: InteractivePropertyEntity): Completable {
        return favoritesDao.deleteCompletable(entity)
    }

    override fun deleteFavoriteEntityForUser(userId: Long, propertyId: Int): Completable {
        return favoritesDao.deleteFavoritesForUserWithId(userId, propertyId)
    }
}
