package com.smarttoolfactory.data.model.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.Relation
import com.smarttoolfactory.data.model.IEntity

/**
 *
 * This is the junction table which for MANY-TO-MANY relation which maps
 * user's id [UserEntity.userId] to [userAccountId], and
 * favorite property's id [FavoritePropertyEntity.id] to [propertyId]
 *
 * * RAW STATEMENT:
 * "CREATE TABLE IF NOT EXISTS `UserFavoriteJunction`
 * (`userAccountId` INTEGER NOT NULL, `propertyId` INTEGER NOT NULL,
 * PRIMARY KEY(`userAccountId`, `propertyId`),
 * FOREIGN KEY(`userAccountId`) REFERENCES `user`(`userId`) ON UPDATE NO ACTION ON DELETE CASCADE ,
 * FOREIGN KEY(`propertyId`) REFERENCES `favorite`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )"
 *
 */
@Entity(
    tableName = "user_favorite_junction",
    primaryKeys = ["userAccountId", "propertyId"],
    // Foreign Keys
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userAccountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FavoritePropertyEntity::class,
            parentColumns = ["id"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserFavoriteJunction(
    val userAccountId: Long,
    val propertyId: Int
) : IEntity

data class UserWithFavorites(

    @Embedded
    val user: UserEntity,

    @Relation(
        parentColumn = "userId",
        entity = FavoritePropertyEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = UserFavoriteJunction::class,
            parentColumn = "userAccountId",
            entityColumn = "propertyId"
        )
    )
    val favorites: List<FavoritePropertyEntity>
)
