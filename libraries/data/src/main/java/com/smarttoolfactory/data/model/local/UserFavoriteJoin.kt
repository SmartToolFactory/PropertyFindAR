package com.smarttoolfactory.data.model.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation
import com.smarttoolfactory.data.model.IEntity

/*
    Tables:
    User - Junction - Property
 */
/**
 *
 * This is the junction table which for MANY-TO-MANY relation which maps
 * user's id [UserEntity.userId] to [userAccountId], and
 * favorite property's id [InteractivePropertyEntity.id] to [propertyId]
 *
 * * RAW STATEMENT:
 * ```
 * "CREATE TABLE IF NOT EXISTS `user_favorite_junction` (
 * `userAccountId` INTEGER NOT NULL,
 * `propertyId` INTEGER NOT NULL,
 * `viewCount` INTEGER NOT NULL,
 * `liked` INTEGER NOT NULL,
 * PRIMARY KEY(`userAccountId`, `propertyId`),
 * FOREIGN KEY(`userAccountId`) REFERENCES `user`(`userId`)
 * ON UPDATE NO ACTION ON DELETE CASCADE ,
 * FOREIGN KEY(`propertyId`) REFERENCES `property_interactive`(`id`)
 * ON UPDATE NO ACTION ON DELETE CASCADE )"
 * ```
 *
 */
@Entity(
    tableName = "user_favorite_junction",
    primaryKeys = ["userAccountId", "propertyId"],
    indices = [Index("userAccountId", "propertyId")],
    // Foreign Keys
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userAccountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = InteractivePropertyEntity::class,
            parentColumns = ["id"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class UserFavoriteJunction(
    val userAccountId: Long,
    val propertyId: Int,
    val viewCount: Int = 0,
    val liked: Boolean = false
) : IEntity

data class UserWithProperties(

    @Embedded
    val user: UserEntity,

    @Relation(
        // User table value
        parentColumn = "userId",
        entity = InteractivePropertyEntity::class,
        // Property table value
        entityColumn = "id",
        associateBy = Junction(
            value = UserFavoriteJunction::class,
            parentColumn = "userAccountId",
            entityColumn = "propertyId"
        )
    )
    val properties: List<InteractivePropertyEntity>
)

/**
 * This relation can be used show properties are favored/liked and/or viewed by a user with
 * specific id and returns this user.
 */
data class UserWithPropertyInteractions(

    @Embedded
    val user: UserEntity,

    @Relation(parentColumn = "userId", entityColumn = "userAccountId")
    val propertyStatusList: List<UserFavoriteJunction>
)

/**
 * Get a property user interacted with and it's status  using
 * [InteractivePropertyEntity] table for query.
 *
 * * Returns a list of [UserFavoriteJunction], because more than
 * one user may have interacted with same property.
 */
data class PropertyAndStatus(

    @Embedded
    val property: InteractivePropertyEntity,

    @Relation(parentColumn = "id", entityColumn = "propertyId")
    val propertyStatusList: List<UserFavoriteJunction>
)

/**
 * Alternative to [PropertyAndStatus] using [UserFavoriteJunction] table for query with user id
 * and retrieves ***single*** [InteractivePropertyEntity] with ***single status***.
 */
data class PropertyWithFavorites(
    val viewCount: Int,
    val liked: Boolean,
    val propertyId: Long,
    @Relation(
        parentColumn = "propertyId",
        entityColumn = "id"
    )
    val property: InteractivePropertyEntity
) : IEntity
