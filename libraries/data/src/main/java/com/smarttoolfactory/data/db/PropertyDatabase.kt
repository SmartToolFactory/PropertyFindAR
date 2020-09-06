package com.smarttoolfactory.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smarttoolfactory.data.constant.DATABASE_VERSION
import com.smarttoolfactory.data.model.local.PropertyEntity

@Database(
    entities = [PropertyEntity::class],
    version = DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(PropertyTypeConverters::class)
abstract class PropertyDatabase : RoomDatabase() {

    abstract fun propertyDaoCoroutines(): PropertyDaoCoroutines

    abstract fun propertyDaoRxJava(): PropertyDaoRxJava3
}
