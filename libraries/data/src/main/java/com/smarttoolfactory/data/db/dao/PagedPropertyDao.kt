package com.smarttoolfactory.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.smarttoolfactory.data.model.local.PagedPropertyEntity

@Dao
interface PagedPropertyDao : BaseCoroutinesDao<PagedPropertyEntity> {

    @Delete
    suspend fun deletePagedPropertyEntity(entity: PagedPropertyEntity): Int

    @Query("DELETE FROM paged_property")
    suspend fun deleteAll()

    /**
     * Get number of properties in db
     */
    @Query("SELECT COUNT(*) FROM paged_property")
    suspend fun getPropertyCount(): Int

    /**
     * Get properties from database.
     *
     * *If database is empty returns empty list []
     */
    @Query("SELECT * FROM paged_property")
    suspend fun getPropertyList(): List<PagedPropertyEntity>
}
