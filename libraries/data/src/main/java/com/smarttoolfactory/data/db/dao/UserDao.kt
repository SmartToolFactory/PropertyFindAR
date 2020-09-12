package com.smarttoolfactory.data.db.dao

import androidx.room.Dao
import com.smarttoolfactory.data.model.local.UserEntity

/**
 * This interface is used for CRUD operation on database
 */
@Dao
interface UserDao : BaseCoroutinesDao<UserEntity>
