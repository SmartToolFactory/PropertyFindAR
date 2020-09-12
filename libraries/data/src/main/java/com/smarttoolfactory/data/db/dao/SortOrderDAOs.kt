package com.smarttoolfactory.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smarttoolfactory.data.model.local.SortOrderEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface SortOrderDaoCoroutines {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SortOrderEntity): Long

    @Query("SELECT order_by FROM sort_order")
    suspend fun getSortOrderEntity(): String
}

@Dao
interface SortOrderDaoRxJava3 {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(propertyEntity: SortOrderEntity): Completable

    @Query("SELECT order_by FROM sort_order")
    fun getSortOrderSingle(): Single<String>
}
