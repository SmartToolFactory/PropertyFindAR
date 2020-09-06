package com.smarttoolfactory.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smarttoolfactory.data.model.local.PropertyEntity

@Database(
    entities = [PropertyEntity::class],
    version = 1,
    exportSchema = true
)
abstract class PropertyDatabase : RoomDatabase() {
    abstract fun propertyDaoCoroutines(): PropertyDaoCoroutines
    abstract fun propertyDaoRxJava(): PropertyDaoRxJava3
}
