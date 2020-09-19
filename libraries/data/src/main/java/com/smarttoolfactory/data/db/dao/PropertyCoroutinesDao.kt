package com.smarttoolfactory.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.smarttoolfactory.data.model.local.PropertyEntity

@Dao
interface PropertyCoroutinesDao : BaseCoroutinesDao<PropertyEntity> {

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
