package com.smarttoolfactory.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.smarttoolfactory.data.constant.DATABASE_VERSION
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.local.SortOrderEntity

@Database(
    entities = [PropertyEntity::class, SortOrderEntity::class],
    version = DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(PropertyTypeConverters::class)
abstract class PropertyDatabase : RoomDatabase() {

    abstract fun propertyDaoCoroutines(): PropertyDaoCoroutines

    abstract fun propertyDaoRxJava(): PropertyDaoRxJava3

    abstract fun propertySortDaoCoroutines(): SortOrderDaoCoroutines

    abstract fun propertySortDaoRxJava(): SortOrderDaoRxJava3
}

/**
 * In this migration add sort by key to database to persist on after closing the app
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
