package com.smarttoolfactory.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smarttoolfactory.data.model.local.PropertyEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface PropertyDaoRxJava3 {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(propertyEntity: PropertyEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(propertyEntityList: List<PropertyEntity>): Completable

    @Delete
    fun deleteProperty(entity: PropertyEntity): Single<Int>

    @Query("DELETE FROM property")
    fun deleteAll(): Completable

    /**
     * Get number of properties in db
     */
    @Query("SELECT COUNT(*) FROM property")
    fun getPropertyCount(): Maybe<Int>

    /**
     * Get list of [PropertyEntity]s to from database.
     */
    @Query("SELECT * FROM property")
    fun getPropertiesSingle(): Single<List<PropertyEntity>>

    /**
     * Get list of [PropertyEntity]s to from database.
     */
    @Query("SELECT * FROM property")
    fun getPropertiesMaybe(): Maybe<List<PropertyEntity>>

    /**
     * Get list of [PropertyEntity]s to from database.
     */
    @Query("SELECT * FROM property")
    fun getProperties(): Observable<List<PropertyEntity>>
}
