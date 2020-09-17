package com.smarttoolfactory.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.smarttoolfactory.data.model.local.InteractivePropertyEntity
import com.smarttoolfactory.data.model.local.PropertyAndStatus
import com.smarttoolfactory.data.model.local.PropertyWithFavorites
import com.smarttoolfactory.data.model.local.UserEntity
import com.smarttoolfactory.data.model.local.UserFavoriteJunction
import com.smarttoolfactory.data.model.local.UserWithProperties

/**
 *
 *  Dao for retrieving favorite items belong to a user with specific id
 *  using [UserFavoriteJunction] junction table or query with ```JOIN```
 *
 */
@Dao
abstract class FavoritesCoroutinesDao : BaseCoroutinesDao<InteractivePropertyEntity> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertInteractiveProperty(
        property: InteractivePropertyEntity
    ): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertUserFavoriteJoin(
        userFavoriteJunction: UserFavoriteJunction
    ): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertUserFavoriteJoin(list: List<UserFavoriteJunction>)

    // Transaction executes operations as a Single Atomic Operation
    // 🔥 This method should be open to work with @Transaction

    /**
     * Inserts 1 favorite into `favorite` table and [UserEntity.userId]
     * and [InteractivePropertyEntity.id] into [UserFavoriteJunction] only if user with same id
     * is available in `user` table [UserEntity]
     *
     * @return id of inserted favorite property id
     */
    @Transaction
    open suspend fun insertUserFavorite(
        userId: Long,
        favoritePropertyEntity: InteractivePropertyEntity,
        viewCount: Int = 0,
        like: Boolean = false
    ): Long {

        // Add to favorites table
        val result = insertInteractiveProperty(favoritePropertyEntity)

        val userFavoriteJunction = UserFavoriteJunction(
            userAccountId = userId,
            propertyId = favoritePropertyEntity.id,
            viewCount = viewCount,
            liked = like
        )

        // Add to junction table
        insertUserFavoriteJoin(userFavoriteJunction)

        return result
    }

    @Delete
    abstract suspend fun deleteInteractiveProperty(
        favoritePropertyEntity: InteractivePropertyEntity
    ): Int

    /**
     * Get contents of user and favorite junction which are
     * userId, propertyId, viewCount, and favorite for every user
     */
    @Transaction
    @Query("SELECT * FROM user_favorite_junction")
    abstract suspend fun getUserFavoriteJunction(): List<UserFavoriteJunction>

    /**
     * Get contents of user and favorite junction which are
     * userId, propertyId, viewCount, and favorite for user with id [userId]
     */
    @Transaction
    @Query("SELECT * FROM user_favorite_junction WHERE userAccountId =:userId")
    abstract suspend fun getUserFavoriteJunction(
        userId: Long
    ): List<UserFavoriteJunction>

    @Transaction
    @Query(
        "SELECT * FROM user_favorite_junction " +
            "WHERE userAccountId =:userId AND propertyId =:propertyId"
    )
    abstract suspend fun getUserFavoriteJunction(
        userId: Long,
        propertyId: Int
    ): UserFavoriteJunction?

    /**
     * Get properties that users had any interaction such as checking it's details or setting
     * it's like/update status
     */
    @Transaction
    @Query("SELECT * FROM user")
    abstract suspend fun getUsersWithProperties(): List<UserWithProperties>

    /**
     * Get property for user with [id] had any interaction such as checking it's details or setting
     * it's like/favorite status.
     *
     * * Contains [UserEntity] and list of [InteractivePropertyEntity]
     */
    @Transaction
    @Query("SELECT * FROM user WHERE userId =:id")
    abstract suspend fun getUserWithProperties(id: Long): UserWithProperties?

    /**
     * Query properties that has been viewed or/and liked and combine them with status
     * from [UserFavoriteJunction] table with matching property ids.
     *
     *@return list of [PropertyAndStatus] which is combination of
     * [InteractivePropertyEntity] and list of [UserFavoriteJunction])
     */
    @Transaction
    @Query("SELECT * FROM property_interactive")
    abstract suspend fun getPropertiesAndStatus(): List<PropertyAndStatus>

    /**
     * Query a property using [InteractivePropertyEntity.id] and combine it with status
     * from [UserFavoriteJunction] table with matching property id.
     *
     * @return [InteractivePropertyEntity] and list of [UserFavoriteJunction]
     */
    @Transaction
    @Query("SELECT * FROM property_interactive WHERE id =:propertyId")
    abstract suspend fun getPropertyAndStatus(propertyId: Int): PropertyAndStatus?

    /**
     * Query a property and it's like and view count status from ***juntion table***
     *
     * @return viewCount, like, and [InteractivePropertyEntity]
     */
    @Transaction
    @Query("SELECT * FROM user_favorite_junction as j where j.userAccountId =:userId")
    abstract suspend fun getPropertiesWithFavorites(userId: Long): List<PropertyWithFavorites>

    /**
     * JOIN query that returns favorite  Properties selected by user with [UserEntity.userId]
     * @param id of the user that has favorites that should
     * match ***user_favorite_junction.userAccountId***
     *
     * @return favorite property entities belong to this user
     */
    @Transaction
    @Query(
        "SELECT * FROM user_favorite_junction  " +
            "INNER JOIN user ON user.userId = user_favorite_junction.userAccountId " +
            "INNER JOIN property_interactive ON " +
            "user_favorite_junction.propertyId = property_interactive.id " +
            "WHERE user_favorite_junction.userAccountId =:id"
    )
    abstract suspend fun getInteractiveProperties(id: Long): List<InteractivePropertyEntity>

    @Query("SELECT * FROM property_interactive")
    abstract suspend fun getInteractiveProperties(): List<InteractivePropertyEntity>

    @Transaction
    @Query(
        "DELETE FROM user_favorite_junction " +
            "WHERE user_favorite_junction.userAccountId =:userId " +
            "AND user_favorite_junction.propertyId =:propertyId"
    )
    abstract suspend fun deleteFavoriteEntityForUser(userId: Long, propertyId: Int)
}
