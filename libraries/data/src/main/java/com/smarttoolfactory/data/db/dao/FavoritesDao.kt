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
 *  Dao for retrieving favorite items belong to a user with specific id.
 *
 */
@Dao
abstract class FavoritesDao {

    @Insert
    abstract suspend fun insertFavorite(favoritePropertyEntity: FavoritePropertyEntity): Long

    @Insert
    abstract suspend fun insertUserFavoriteJoin(userFavoriteJunction: UserFavoriteJunction)

    // Transaction executes operations as a Single Atomic Operation
    // 🔥 This method should be open to work with @Transaction
    @Transaction
    open suspend fun insertUserFavorite(
        userId: Long,
        favoritePropertyEntity: FavoritePropertyEntity
    ) {

        // Add to favorites table
        insertFavorite(favoritePropertyEntity)

        // Add to junction table
        insertUserFavoriteJoin(
            UserFavoriteJunction(
                userAccountId = userId,
                propertyId = favoritePropertyEntity.id
            )
        )
    }

    @Delete
    abstract suspend fun deleteFavorite(favoritePropertyEntity: FavoritePropertyEntity): Int

    @Transaction
    @Query("SELECT * FROM user WHERE userId =:id")
    abstract suspend fun getFavorites(id: Int): List<UserWithFavorites>

    /**
     * JOIN query that returns favorite  Properties selected by user with [UserEntity.userId]
     * @param id of the user that has favorites
     * @return favorite property entities belong to this user
     */
    @Transaction
    @Query(
        "SELECT * FROM favorite " +
            "INNER JOIN user ON user.userId = favorite.id WHERE user.userId =:id"
    )
    abstract suspend fun getFavoritesOfUser(id: Long): List<FavoritePropertyEntity>
}
