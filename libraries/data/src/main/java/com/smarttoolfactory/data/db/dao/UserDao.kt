package com.smarttoolfactory.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.smarttoolfactory.data.model.local.UserEntity

/**
 * This interface is used for CRUD operation on database
 */
@Dao
interface UserDao : BaseCoroutinesDao<UserEntity> {

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM user WHERE user.userId =:id")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}
