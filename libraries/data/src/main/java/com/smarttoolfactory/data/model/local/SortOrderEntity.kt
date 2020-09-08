package com.smarttoolfactory.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smarttoolfactory.data.constant.ORDER_BY_NONE

@Entity(tableName = "sort_order")
data class SortOrderEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "order_by", defaultValue = ORDER_BY_NONE)
    val orderBy: String = ORDER_BY_NONE
)
