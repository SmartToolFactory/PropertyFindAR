package com.smarttoolfactory.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smarttoolfactory.data.model.IEntity

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
) : IEntity
