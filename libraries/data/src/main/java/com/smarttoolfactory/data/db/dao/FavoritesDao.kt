package com.smarttoolfactory.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.smarttoolfactory.data.model.local.FavoritePropertyEntity
import com.smarttoolfactory.data.model.local.UserEntity
import com.smarttoolfactory.data.model.local.UserFavoriteJunction
import com.smarttoolfactory.data.model.local.UserWithFavorites

/**
 *
 *  Dao for retrieving favorite items belong to a user with specific id
 *  using [UserFavoriteJunction] junction table or query with ```JOIN```
 *
 */
@Dao
abstract class FavoritesDao {

    @Insert
    abstract suspend fun insertFavorite(favoritePropertyEntity: FavoritePropertyEntity): Long

    @Insert
    abstract suspend fun insertFavorites(
        favoritePropertyEntities: List<FavoritePropertyEntity>
    ): List<Long>

    @Insert
    abstract suspend fun insertUserFavoriteJoin(userFavoriteJunction: UserFavoriteJunction)

    // Transaction executes operations as a Single Atomic Operation
    // 🔥 This method should be open to work with @Transaction

    /**
     * Inserts 1 favorite into `favorite` table and [UserEntity.userId]
     * and [FavoritePropertyEntity.id] into [UserFavoriteJunction] only if user with same id
     * is available in `user` table [UserEntity]
     *
     * @return id of inserted favorite property id
     */
    @Transaction
    open suspend fun insertUserFavorite(
        userId: Long,
        favoritePropertyEntity: FavoritePropertyEntity
    ): Long {

        // Add to favorites table
        val result = insertFavorite(favoritePropertyEntity)

        // Add to junction table
        insertUserFavoriteJoin(
            UserFavoriteJunction(
                userAccountId = userId,
                propertyId = favoritePropertyEntity.id
            )
        )

        return result
    }

    // Transaction executes operations as a Single Atomic Operation
    // 🔥 This method should be open to work with @Transaction

    /**
     * Inserts list of favorites into `favorite` table and [UserEntity.userId]
     * and [FavoritePropertyEntity.id] into [UserFavoriteJunction] only if user with same id
     * is available in `user` table [UserEntity]
     *
     * @return list of ids of favorite properties inserted with this transaction
     */
    @Transaction
    open suspend fun insertUserFavoritesList(
        userId: Long,
        list: List<FavoritePropertyEntity>
    ): List<Long> {

        // Add to favorites table
        val result = insertFavorites(list)

        // Add to junction table
        list.forEach { favoritePropertyEntity ->
            insertUserFavoriteJoin(
                UserFavoriteJunction(
                    userAccountId = userId,
                    propertyId = favoritePropertyEntity.id
                )
            )
        }

        return result
    }

    @Delete
    abstract suspend fun deleteFavorite(favoritePropertyEntity: FavoritePropertyEntity): Int

    @Query("SELECT * FROM user_favorite_junction")
    abstract suspend fun getUserFavoriteJunction(): List<UserFavoriteJunction>

    @Transaction
    @Query("SELECT * FROM user")
    abstract suspend fun getUsersAndFavorites(): List<UserWithFavorites>

    @Transaction
    @Query("SELECT * FROM user WHERE userId =:id")
    abstract suspend fun getUserByIdAndFavorites(id: Long): List<UserWithFavorites>

    /**
     * JOIN query that returns favorite  Properties selected by user with [UserEntity.userId]
     * @param id of the user that has favorites that should
     * match ***user_favorite_junction.userAccountId***
     *
     * @return favorite property entities belong to this user
     */
    @Transaction
    @Query(
        "SELECT * FROM user_favorite_junction " +
            "INNER JOIN user ON user.userId = user_favorite_junction.userAccountId " +
            "INNER JOIN favorite ON user_favorite_junction.propertyId = favorite.id " +
            "WHERE user_favorite_junction.userAccountId =:id"
    )
    abstract suspend fun getFavoritesOfUser(id: Long): List<FavoritePropertyEntity>
}
