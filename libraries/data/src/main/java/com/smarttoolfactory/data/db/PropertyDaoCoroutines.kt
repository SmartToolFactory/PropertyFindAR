package com.smarttoolfactory.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smarttoolfactory.data.model.local.PropertyEntity

@Dao
interface PropertyDaoCoroutines {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PropertyEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<PropertyEntity>): List<Long>

    @Delete
    suspend fun deletePropertyEntity(entity: PropertyEntity): Int

    @Query("DELETE FROM property")
    suspend fun deleteAll()

    /**
     * Get number of properties in db
     */
    @Query("SELECT COUNT(*) FROM property")
    suspend fun getPropertyCount(): Int

    /**
     * Get properties from database.
     *
     * *If database is empty returns empty list []
     */
    @Query("SELECT * FROM property")
    suspend fun getPropertyList(): List<PropertyEntity>
}
