package com.smarttoolfactory.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.smarttoolfactory.data.constant.DATABASE_VERSION
import com.smarttoolfactory.data.db.converters.PropertyTypeConverters
import com.smarttoolfactory.data.db.dao.FavoritesDao
import com.smarttoolfactory.data.db.dao.PagedPropertyDao
import com.smarttoolfactory.data.db.dao.PropertyDaoCoroutines
import com.smarttoolfactory.data.db.dao.PropertyDaoRxJava3
import com.smarttoolfactory.data.db.dao.SortOrderDaoCoroutines
import com.smarttoolfactory.data.db.dao.SortOrderDaoRxJava3
import com.smarttoolfactory.data.db.dao.UserDao
import com.smarttoolfactory.data.model.local.FavoritePropertyEntity
import com.smarttoolfactory.data.model.local.PagedPropertyEntity
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.local.SortOrderEntity
import com.smarttoolfactory.data.model.local.UserEntity
import com.smarttoolfactory.data.model.local.UserFavoriteJunction

@Database(
    entities = [
        PropertyEntity::class,
        PagedPropertyEntity::class,
        SortOrderEntity::class,
        FavoritePropertyEntity::class,
        UserEntity::class,
        UserFavoriteJunction::class
    ],
    version = DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(PropertyTypeConverters::class)
abstract class PropertyDatabase : RoomDatabase() {

    abstract fun propertyDaoCoroutines(): PropertyDaoCoroutines

    abstract fun propertyDaoRxJava(): PropertyDaoRxJava3

    abstract fun propertySortDaoCoroutines(): SortOrderDaoCoroutines

    abstract fun propertySortDaoRxJava(): SortOrderDaoRxJava3

    abstract fun pagedPropertyDao(): PagedPropertyDao

    abstract fun userDao(): UserDao

    abstract fun favoritesDao(): FavoritesDao
}

/**
 * Add sortBy key to database to persist it after closing the app
 */
val MIGRATION_1_2: Migration = object : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS sort_filter " +
                "(id INTEGER PRIMARY KEY  NOT NULL, " +
                "order_by TEXT NOT NULL DEFAULT '')"
        )
    }
}

/**
 * Add new Property table for paging, this could have been done with [PropertyEntity] but
 * used this as another sample.
 */
val MIGRATION_2_3: Migration = object : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `paged_property` (" +
                "`insert_order` INTEGER NOT NULL, " +
                "`id` INTEGER NOT NULL, " +
                "`update` INTEGER NOT NULL, " +
                "`category_id` INTEGER NOT NULL, " +
                "`title` TEXT NOT NULL, " +
                "`subject` TEXT NOT NULL, " +
                "`type` TEXT NOT NULL, " +
                "`type_id` INTEGER NOT NULL, " +
                "`thumbnail` TEXT, " +
                "`thumbnail_big` TEXT, " +
                "`image_count` INTEGER NOT NULL, " +
                "`price` TEXT NOT NULL, " +
                "`price_period` TEXT, " +
                "`price_period_raw` TEXT NOT NULL, " +
                "`price_label` TEXT, " +
                "`price_value` TEXT, " +
                "`price_value_raw` INTEGER NOT NULL, " +
                "`currency` TEXT NOT NULL, " +
                "`featured` INTEGER NOT NULL, " +
                "`location` TEXT NOT NULL, " +
                "`area` TEXT NOT NULL, " +
                "`poa` INTEGER NOT NULL, " +
                "`rera_permit` TEXT, " +
                "`bathrooms` TEXT NOT NULL, " +
                "`bedrooms` TEXT NOT NULL, " +
                "`date_insert` TEXT NOT NULL, " +
                "`date_update` TEXT NOT NULL, " +
                "`agent_name` TEXT NOT NULL, " +
                "`broker_name` TEXT NOT NULL, " +
                "`agent_license` TEXT, " +
                "`location_id` INTEGER NOT NULL, " +
                "`hide_location` INTEGER NOT NULL, " +
                "`broker` TEXT NOT NULL, " +
                "`amenities` TEXT NOT NULL, " +
                "`amenities_keys` TEXT NOT NULL, " +
                "`latitude` REAL NOT NULL, " +
                "`longitude` REAL NOT NULL, " +
                "`premium` INTEGER NOT NULL, " +
                "`livingrooms` TEXT NOT NULL, " +
                "`verified` INTEGER NOT NULL, " +
                "`gallery` TEXT, " +
                "`phone` TEXT NOT NULL, " +
                "`lead_email_receivers` TEXT NOT NULL, " +
                "`reference` TEXT NOT NULL, PRIMARY KEY(`insert_order`))"
        )
    }
}

/**
 *
 * Contents of this migration are
 *
 * * Add new Property table for favorite properties [FavoritePropertyEntity].
 *
 * * Add user table for managing login, register and accounts table with [UserEntity]
 *
 * * Add junction table for connecting User with his/her favorite items
 */
val MIGRATION_3_4: Migration = object : Migration(3, 4) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(STATEMENT_FAVORITE_PROPERTIES)

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `user` (" +
                "`userId` INTEGER NOT NULL, " +
                "`firstName` TEXT NOT NULL, " +
                "`lastName` TEXT NOT NULL, " +
                "`email` TEXT NOT NULL, " +
                "`password` TEXT NOT NULL, PRIMARY KEY(`userId`))"
        )

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `user_favorite_junction` (" +
                "`userAccountId` INTEGER NOT NULL, " +
                "`propertyId` INTEGER NOT NULL, " +
                "PRIMARY KEY(`userAccountId`, `propertyId`), " +
                "FOREIGN KEY(`userAccountId`) REFERENCES `user`(`userId`) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE , " +
                "FOREIGN KEY(`propertyId`) REFERENCES `favorite`(`id`) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE )"
        )
    }
}
/*
    Changed to String because detekt has 60 lines limit for methods
 */
const val STATEMENT_FAVORITE_PROPERTIES = "CREATE TABLE IF NOT EXISTS `favorite` (" +
    "`id` INTEGER NOT NULL, " +
    "`update` INTEGER NOT NULL, " +
    "`categoryId` INTEGER NOT NULL, " +
    "`title` TEXT NOT NULL, " +
    "`subject` TEXT NOT NULL, `" +
    "type` TEXT NOT NULL, " +
    "`typeId` INTEGER NOT NULL, " +
    "`thumbnail` TEXT, " +
    "`thumbnailBig` TEXT, " +
    "`imageCount` INTEGER NOT NULL, " +
    "`price` TEXT NOT NULL, " +
    "`pricePeriod` TEXT, " +
    "`pricePeriodRaw` TEXT NOT NULL, " +
    "`priceLabel` TEXT, " +
    "`priceValue` TEXT, " +
    "`priceValueRaw` INTEGER NOT NULL, " +
    "`currency` TEXT NOT NULL, " +
    "`featured` INTEGER NOT NULL, " +
    "`location` TEXT NOT NULL, " +
    "`area` TEXT NOT NULL, " +
    "`poa` INTEGER NOT NULL, " +
    "`reraPermit` TEXT, " +
    "`bathrooms` TEXT NOT NULL, " +
    "`bedrooms` TEXT NOT NULL, " +
    "`dateInsert` TEXT NOT NULL, " +
    "`dateUpdate` TEXT NOT NULL, " +
    "`agentName` TEXT NOT NULL, " +
    "`brokerName` TEXT NOT NULL, " +
    "`agentLicense` TEXT, " +
    "`locationId` INTEGER NOT NULL, " +
    "`hideLocation` INTEGER NOT NULL, " +
    "`broker` TEXT NOT NULL, " +
    "`amenities` TEXT NOT NULL, " +
    "`amenitiesKeys` TEXT NOT NULL, " +
    "`latitude` REAL NOT NULL, " +
    "`longitude` REAL NOT NULL, " +
    "`premium` INTEGER NOT NULL, " +
    "`livingrooms` TEXT NOT NULL, " +
    "`verified` INTEGER NOT NULL, " +
    "`gallery` TEXT, " +
    "`phone` TEXT NOT NULL, " +
    "`leadEmailReceivers` TEXT NOT NULL, " +
    "`reference` TEXT NOT NULL, " +
    "`insert_date` INTEGER NOT NULL, " +
    "`display_count` INTEGER NOT NULL, " +
    "`favorite` INTEGER NOT NULL, " +
    "PRIMARY KEY(`id`))"
